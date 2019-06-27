package com.followapp.core.imimobile;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.followapp.core.model.CallDetails;
import com.followapp.core.services.ICallingServiceApi;

@Component
public class ImiMobileApi implements ICallingServiceApi {

    private static final Logger LOG = LoggerFactory.getLogger(ImiMobileApi.class);

    private final String key;

    private final String domain;

    public ImiMobileApi(@Value("${app.imimobile.key}") String key,
            @Value("${app.domainName}") String domain) {
        this.key = key;        
        this.domain = domain;
    }

    public String call(String phoneNumber, List<String> audioFiles, CallDetails callDetails) {
        try (CloseableHttpClient client = HttpClientBuilder.create()
                .setRetryHandler(new DefaultHttpRequestRetryHandler()).build()) {
            HttpPost callRequest = null;
            try {
                callRequest = createRequest(phoneNumber, audioFiles, callDetails);
                LOG.info("Request {}", callRequest);
                Optional<HttpResponse> callResponse = Optional.ofNullable(client.execute(callRequest));

                int statusCode = callResponse.map(response -> response.getStatusLine())
                        .map(statusLine -> statusLine.getStatusCode()).orElse(HttpStatus.SC_METHOD_FAILURE);
                String uuid = callResponse.map(response -> response.getEntity()).map(entity -> getResponse(entity))
                        .orElse(null);

                if (statusCode == HttpStatus.SC_OK && StringUtils.isNotBlank(uuid)) {
                    uuid = StringUtils.replace(uuid, "Success,", StringUtils.EMPTY);
                    LOG.info("Outgoing call to {} was successful. Uuid {}", phoneNumber, uuid);
                    return StringUtils.trim(uuid);
                } else {
                    throw new ImiMobileApiException(String.format("Outgoing call to %s failed. Status code %s. %s",
                            phoneNumber, statusCode, callResponse.map(response -> response.getStatusLine())
                                    .map(statusLine -> statusLine.getReasonPhrase()).orElse(StringUtils.EMPTY)));
                }
            } catch (IOException exception) {
                throw new ImiMobileApiException(String.format("Outgoing call to %s failed", phoneNumber), exception);
            } finally {
                if (callRequest != null) {
                    callRequest.releaseConnection();
                }
            }
        } catch (IOException exception) {
            throw new ImiMobileApiException(String.format("Outgoing call to %s failed", phoneNumber), exception);
        }
    }

    private String getResponse(HttpEntity entity) {
        try {
            return IOUtils.toString(entity.getContent(), Charset.defaultCharset());
        } catch (UnsupportedOperationException | IOException exception) {
            throw new ImiMobileApiException("Failure while processing response", exception);
        }
    }

    private HttpPost createRequest(String phoneNumber, List<String> audioFiles, CallDetails callDetails) throws UnsupportedEncodingException {
        HttpPost callRequest = new HttpPost("http://api-openhouse.imimobile.com/1/obd/thirdpartycall/callSessions");
        callRequest.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

        callRequest.addHeader("key", this.key);
        List<NameValuePair> postParams = new ArrayList<>();
        postParams.add(new BasicNameValuePair("address", phoneNumber));
        postParams.add(new BasicNameValuePair("mode", "Callflow"));        
        postParams.add(new BasicNameValuePair("callflow_id", callDetails.getCallFlowId()));			//retrieving value dynamically
        postParams.add(new BasicNameValuePair("externalHeaders", getExternalHeaders(audioFiles)));
        postParams.add(new BasicNameValuePair("externalParams", "_esb_trans_id,_errorcode,_dtmf"));        
        postParams.add(new BasicNameValuePair("callbackurl", String.join(this.domain, callDetails.getCallbackEndPoint())));	//retrieving value dynamically

        callRequest.setEntity(new UrlEncodedFormEntity(postParams));
        return callRequest;
    }

    private String getExternalHeaders(List<String> audioFiles) {
        StringJoiner externalHeaders = new StringJoiner(";");
        int i = 1;
        for (String audioFile : audioFiles) {
            externalHeaders.add(String.format("x-imi-ivrs-v%s:%s", i++, audioFile));
        }
        return externalHeaders.toString();
    }
}


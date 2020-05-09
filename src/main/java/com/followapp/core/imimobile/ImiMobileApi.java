package com.followapp.core.imimobile;


import com.followapp.core.services.CallingServiceApiAttributes;
import com.followapp.core.services.ICallingServiceApi;
import com.followapp.core.services.IMessagingServiceApi;
import com.google.gson.Gson;
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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

@Component
public class ImiMobileApi implements ICallingServiceApi, IMessagingServiceApi {

    private static final Logger LOG = LoggerFactory.getLogger(ImiMobileApi.class);

    private final String key;

    private final String domain;

    private final String senderName;

    private final String senderAddress;

    private final Gson gson = new Gson();

    public ImiMobileApi(@Value("${app.imimobile.key}") String key,
                        @Value("${app.domainName}") String domain,
                        @Value("${app.message.senderName:SNEHAG}") String senderName,
                        @Value("${app.message.senderAddress:SNEHAG}") String senderAddress) {
        this.key = key;
        this.domain = domain;
        this.senderName = senderName;
        this.senderAddress = senderAddress;
    }

    @Override
    public String call(String phoneNumber, List<String> audioFiles, CallingServiceApiAttributes callingServiceApiAttributes) {
        try (CloseableHttpClient client = HttpClientBuilder.create()
                .setRetryHandler(new DefaultHttpRequestRetryHandler()).build()) {
            HttpPost callRequest = null;
            try {
                callRequest = createRequest(phoneNumber, audioFiles, callingServiceApiAttributes.getCallFlowId(), callingServiceApiAttributes.getCallBackEndpoint());
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

    /**
     * Sample response from IMI sms service
     * {
     *   "outboundSMSMessageRequest": {
     *     "deliveryInfoList": {
     *       "deliveryInfo": {
     *         "address": "7405280110",
     *         "deliveryStatus": "Submitted"
     *       },
     *       "resourceURL": "http://api-openhouse.imimobile.com/smsmessaging/1/outbound/SNEHAG/requests/urn:uuid:46f415df-155f-4eea-8ed7-91b87dd15f88/deliveryInfos"
     *     },
     *     "senderAddress": "SNEHAG",
     *     "outboundSMSTextMessage": {
     *       "message": "Test Message"
     *     },
     *     "clientCorrelator": "2764b266-d81d-4e45-a6eb-5c071150399e",
     *     "receiptRequest": {
     *       "notifyURL": "",
     *       "callbackData": ""
     *     },
     *     "senderName": "Meherzad",
     *     "resourceURL": "http://api-openhouse.imimobile.com/smsmessaging/1/outbound/SNEHAG/requests/urn:uuid:46f415df-155f-4eea-8ed7-91b87dd15f88"
     *   }
     * }
     */
    @Override
    public String sendMessage(String phoneNumber, String messageText) {
        if (StringUtils.length(phoneNumber) != 10) {
            throw new ImiMobileApiException("Supports only 10 digit cell phone number " + phoneNumber);
        }
        String stringUrl = String.format("http://api-openhouse.imimobile.com/smsmessaging/1/outbound/%s/requests",
                URLEncoder.encode(formatPhoneNumber(senderAddress, true)));
        String requestBody = getRequestBody(phoneNumber, messageText);
        String outResponse = null;
        try {
            LOG.info("Sending message to {} requestBody {}", phoneNumber, requestBody);
            URL url = new URL(stringUrl);
            HttpURLConnection objReq = (HttpURLConnection) url.openConnection();
            objReq.setRequestMethod("POST");
            objReq.setReadTimeout(50000);
            objReq.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            objReq.setDoOutput(true);
            objReq.setDoInput(true);
            objReq.addRequestProperty("key", key);
            objReq.connect();

            DataOutputStream out = new DataOutputStream(objReq.getOutputStream());
            out.writeBytes(requestBody);
            out.flush();
            out.close();

            int userResponseCode = objReq.getResponseCode();
            InputStream createUserInputStream = (InputStream) objReq.getInputStream();
            StringWriter createUserwriter = new StringWriter();
            IOUtils.copy(createUserInputStream, createUserwriter, Charset.defaultCharset());
            outResponse = createUserwriter.toString();
            LOG.info("Successfully submitted sms request for phoneNumber {}, status {}, outResponse {}", phoneNumber, userResponseCode, outResponse);
            Map map = gson.fromJson(outResponse, Map.class);
            Optional<Map> response = map.values().stream().findFirst().filter(Map.class::isInstance).map(Map.class::cast);
            outResponse = response.map(resp -> Objects.toString(resp.get("resourceURL"), null))
                    .map(d-> StringUtils.substring(d, StringUtils.lastIndexOf(d, "/")+1)).orElse("");
            return outResponse;
        } catch (Exception exception) {
            LOG.error(String.format("Failure while sending message - %s - %s", phoneNumber, outResponse), exception);
            throw new ImiMobileApiException(String.format("Failure while sending message - %s - %s", phoneNumber, outResponse), exception);
        }
    }

    private String formatPhoneNumber(String phoneNumber, boolean countryCode) {
        if (countryCode) {
            return String.format("tel:+91%s", phoneNumber);
        } else {
            return String.format("tel:%s", phoneNumber);
        }
    }

    private String getRequestBody(String phoneNumber, String messageText) {
        String strMainContent = String.format(
                "{\"outboundSMSMessageRequest\":{" +
                        "\"address\":[\"%s\"]," +
                        "\"senderAddress\":\"%s\"," +
                        "\"outboundSMSTextMessage\":{\"message\":\"%s\"}," +
                        "\"messageType\":\"0\"," +
                        "\"clientCorrelator\":\"%s\"," +
                        "\"receiptRequest\":\n" +
                        "{\"notifyURL\":\"%s/api/smsresponse\",\n" +
                        "\"callbackData\":\"$(callbackData)\"}," +
                        "\"senderName\":\"%s\"}" +
                        "}", formatPhoneNumber(phoneNumber, false), formatPhoneNumber(senderAddress, false), messageText,
                UUID.randomUUID(), this.domain, this.senderName);
        return strMainContent;
    }

    private String getResponse(HttpEntity entity) {
        try {
            return IOUtils.toString(entity.getContent(), Charset.defaultCharset());
        } catch (UnsupportedOperationException | IOException exception) {
            throw new ImiMobileApiException("Failure while processing response", exception);
        }
    }

    private HttpPost createRequest(String phoneNumber, List<String> audioFiles, String callFlowId, String callBackEndpoint) throws UnsupportedEncodingException {
        HttpPost callRequest = new HttpPost("http://api-openhouse.imimobile.com/1/obd/thirdpartycall/callSessions");
        callRequest.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

        callRequest.addHeader("key", this.key);
        List<NameValuePair> postParams = new ArrayList<>();
        postParams.add(new BasicNameValuePair("address", phoneNumber));
        postParams.add(new BasicNameValuePair("mode", "Callflow"));
        postParams.add(new BasicNameValuePair("callflow_id", callFlowId));            //retrieving value dynamically
        postParams.add(new BasicNameValuePair("externalHeaders", getExternalHeaders(audioFiles)));
        postParams.add(new BasicNameValuePair("externalParams", "_esb_trans_id,_errorcode,_dtmf"));
        postParams.add(new BasicNameValuePair("callbackurl", String.format("%s%s", this.domain, callBackEndpoint)));    //retrieving value dynamically

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


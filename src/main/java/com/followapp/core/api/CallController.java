package com.followapp.core.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.followapp.core.data.CallHistoryDao;
import com.followapp.core.model.CallResult;
import com.followapp.core.model.CallStatus;
import com.followapp.core.model.ImiMobileResponse;
import com.followapp.core.model.ScheduleDetail;
import com.followapp.core.model.ScheduleRun;
import com.followapp.core.model.ScheduleRunStatus;
import com.followapp.core.model.SmsLanguage;
import com.followapp.core.model.sms.DeliveryInfo;
import com.followapp.core.model.sms.DeliveryInfoNotification;
import com.followapp.core.model.sms.DeliveryStatus;
import com.followapp.core.services.CallingService;
import com.followapp.core.services.CallingServiceApiAttributes;
import com.followapp.core.services.ICallingServiceApi;
import com.followapp.core.services.IMessagingServiceApi;
import com.followapp.core.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Root resource (exposed at "API" path)
 */
@RestController
@RequestMapping(value = "api")
public class CallController {

    private static final Logger LOG = LoggerFactory.getLogger(CallController.class);

    @Autowired
    private CallingService callingService;

    @Autowired
    private JdbcBatchItemWriter<ScheduleRun> writer;

    @Autowired
    private CallHistoryDao callHistoryDao;

    @Autowired
    private ICallingServiceApi callingServiceApi;

    @Autowired
    private IMessagingServiceApi messagingServiceApi;

    @Value("${app.test.endpoints.enabled:false}")
    private boolean testEndpointEnabled;

    @Value("{app.imi.call.callBackEndpoint}")
    private String callBackEndpoint;

    @RequestMapping(value = "test", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> testSomething() {
        return ResponseEntity.ok("Hello\nService is up!");
    }

    @RequestMapping(value="testMessage", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> testMessagingService(@RequestBody MessageRequest messageRequest) {
        LOG.info("Request received for testMessage endpoint {}", messageRequest);
        if (!testEndpointEnabled) {
            LOG.info("Test endpoint is not enabled");
            return new ResponseEntity<>("Test endpoint is disabled", HttpStatus.FORBIDDEN);
        }
        if (messageRequest == null || messageRequest.badRequest()) {
            LOG.info("Bad request for testMessage endpoint");
            return new ResponseEntity<>("phoneNumber and message are mandatory argument. " +
                    "Example {\"phoneNumber\":\"0123456789\",\"message\":\"TestMessage\",\"smsLanguage\":\"HINDI\"}", HttpStatus.BAD_REQUEST);
        }
        String requestId = null;
        if (messageRequest.getSmsLanguageEnum() == SmsLanguage.ENGLISH) {
            requestId = this.messagingServiceApi.sendMessage(messageRequest.getPhoneNumber(), messageRequest.getMessage());
        } else {
            requestId = this.messagingServiceApi.sendMessageUnicode(messageRequest.getPhoneNumber(), messageRequest.getMessage());
        }
        return ResponseEntity.ok("Successfully called, requestId " + requestId);
    }

    @RequestMapping(value="testCall", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> testCallService(@RequestBody CallRequest callRequest) {
        LOG.info("Request received for testCallService endpoint {}", callRequest);
        if (!testEndpointEnabled) {
            LOG.info("Test endpoint is not enabled");
            return new ResponseEntity<>("Test endpoint is disabled", HttpStatus.FORBIDDEN);
        }
        if (callRequest == null || callRequest.badRequest()) {
            LOG.info("Bad request for testCallService endpoint");
            return new ResponseEntity<>("phoneNumber and message are mandatory argument. " +
                    "Example {\"phoneNumber\":\"0123456789\",\"audioFiles\":[\"TestAudio\"],\"callFlowId\":\"777\"}", HttpStatus.BAD_REQUEST);
        }
        CallingServiceApiAttributes callingServiceApiAttributes = CallingServiceApiAttributes.aCallingServiceApiAttributes()
                .callFlowId(callRequest.getCallFlowId()).callBackEndpoint(this.callBackEndpoint).build();
        String requestId = this.callingServiceApi.call(callRequest.getPhoneNumber(), callRequest.getAudioFiles(), callingServiceApiAttributes);
        return ResponseEntity.ok("Successfully called, requestId " + requestId);
    }
    /**
     * Makes a call to the specified user, and initialises the audio to be
     * played when the user picks up
     * <p>
     * IF all goes well, sends back a 200 OK If the CallResult cannot be
     * persisted to the db, sends back a 500 INTERNAL_SERVER_ERROR
     */
    @RequestMapping(value = "call", method = RequestMethod.POST)
    public ResponseEntity<String> callUser(@RequestBody ScheduleDetail scheduleDetail) {
        LOG.info("[call] Got JSON input: " + scheduleDetail);

        ScheduleRun scheduleRun = callingService.callUser(scheduleDetail);

        try {
            writer.write(Arrays.asList(scheduleRun));
            LOG.info("Persisted CallResult of UI initiated call to the database: " + scheduleRun.toString());
        } catch (Exception e) {
            LOG.error("The CallResult from the UI initiated call has failed to be persisted in the database. "
                            + "Please enter the details manually into the database. " + scheduleRun.toString() + e.getMessage(),
                    e);
            return new ResponseEntity<String>(
                    "The CallResult from the UI initiated call failed to be persisted to the database: "
                            + scheduleRun.toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok("Call to " + scheduleDetail.getRecipientMobileNumber() + " has been initiated");
    }

    /**
     * Get user response back from Imimobile. As part if this response we will get the information if usered has pressed 1 or 2.
     * Call to this endpoint will come in form of <pre>
     * http://demo7307378.mockable.io/response?sid=1111111111_99999999999&MobileNo=09999999999&DTMF=2
     * </pre>
     */
    @RequestMapping(value = "response", method = RequestMethod.GET)
    public ResponseEntity<String> getUserResponse(@RequestParam("MobileNo") String mobileNumber,
                                                  @RequestParam("DTMF") String input, @RequestHeader("x-imi-ivrs-_esb_trans_id") String uuid) {
        LOG.info("User input received for uuid {} mobile {} input {}", uuid, mobileNumber, input);

        CallResult callResult = new CallResult(uuid, getStatus(StringUtils.trim(input)));
        callHistoryDao.updateCallStatus(callResult);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.parseMediaType("text/dtmf")).body("0");
    }

    /**
     * Get call response back from Imimobile. This response will have a lot of details.
     * Sample response from ImiMobile. We are only interseted in using the callduration from the response.
     * <pre>
     * {@code
     * <response>
     *      <evt-notification>
     *          <evt-info>
     *              <tid>1111111111_99999999999</tid>
     *              <offered-on>2018-06-10 14:54:30.395</offered-on>
     *              <accepted-on>2018-06-10 14:54:34.200</accepted-on>
     *              <answered-on>2018-06-10 14:54:37.016</answered-on>
     *              <released-on>2018-06-10 14:55:08.797</released-on>
     *              <drop-type>100</drop-type>
     *              <drop-reason>INVALID Content-Type OR HTTP TIMEOUT.</drop-reason>
     *              <drop-description>App_initiated_reject</drop-description>
     *              <call-duration>32</call-duration>
     *              <total-pulses>2</total-pulses>
     *              <callresult></callresult>
     *              <externalParams>
     *                  <_esb_trans_id>urn:uuid:db90656e-0060-412a-81f8-de26dc5e4a55</_esb_trans_id>
     *                  <_errorcode>2006</_errorcode>
     *                  <_dtmf>2</_dtmf>
     *              </externalParams>
     *           </evt-info>
     *       </evt-notification>
     * </response>}
     * </pre>
     */
    @RequestMapping(value = "callresponse", method = RequestMethod.POST, produces = "text/dtmf")
    public ResponseEntity<String> getCallResponse(@RequestBody ImiMobileResponse response) {
        LOG.info("Call response received {}", response);
        Optional<ImiMobileResponse> imiMobileResponse = Optional.ofNullable(response);
        String uuid = imiMobileResponse.map(resp -> resp.getUuid()).orElse("-1");
        Integer callDuration = imiMobileResponse.map(resp -> resp.getEvt()).map(evt -> evt.getEvtInfo())
                .map(evtInfo -> StringUtils.trim(evtInfo.getCallDuration()))
                .filter(cd -> StringUtils.isNumeric(cd)).map(cd -> Integer.parseInt(cd)).orElse(-1);

        callHistoryDao.updateCallDuration(uuid, callDuration);
        return ResponseEntity.ok("0");
    }

    /**
     * Sms response from IMI
     *
     * {
     *   "deliveryInfoNotification": {
     *     "message": "TestMessageFromSneha",
     *     "senderAddress": "SNEHAG",
     *     "clientCorrelator": "",
     *     "deliveryInfo": {
     *       "address": "917405280110",
     *       "errorCode": "0",
     *       "deliveryDate": "2020-05-04 02:57:02",
     *       "deliveryStatus": "Delivered"
     *     },
     *     "RequestId": "urn:uuid:39cfe68e-3e37-4296-93f7-6724d4f24413",
     *     "callbackData": "$(callbackData)",
     *     "serviceName": "API Explorer"
     *   }
     * }
     */
    @RequestMapping(value = "smsresponse", method = RequestMethod.POST)
    public ResponseEntity<String> getSmsResponse(@RequestBody String deliveryStatusString) {
        LOG.info("Sms response received {}", deliveryStatusString);
        DeliveryStatus deliveryStatus = JsonUtils.unMarshall(deliveryStatusString, DeliveryStatus.class);
        Optional<String> ivrRequestId = Optional.ofNullable(deliveryStatus).map(DeliveryStatus::deliveryInfoNotification)
                .map(DeliveryInfoNotification::requestId);
        ivrRequestId.ifPresent(ivrRequest -> {
            List<ScheduleRunStatus> messageDeliveryStatus = Optional.ofNullable(deliveryStatus).map(DeliveryStatus::deliveryInfoNotification)
                    .map(DeliveryInfoNotification::deliveryInfo).orElse(Collections.emptyList())
                    .stream().map(DeliveryInfo::deliveryStatus)
                    .map(ScheduleRunStatus::find)
                    .collect(Collectors.toList());
            ScheduleRunStatus scheduleRunStatus = ScheduleRunStatus.aggregate(messageDeliveryStatus);
            callHistoryDao.updateMessageStatus(ivrRequest, scheduleRunStatus.name());
        });
        return ResponseEntity.ok("0");
    }

    private CallStatus getStatus(String response) {
        if (StringUtils.equals(response, "1")) {
            return CallStatus.PRESCRIPTION_TAKEN;
        }
        return CallStatus.PRESCRIPTION_NOT_TAKEN;
    }

    public static class CallRequest {
        private String phoneNumber;
        private List<String> audioFiles;
        private String callFlowId;

        public String getPhoneNumber() {
            return this.phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public List<String> getAudioFiles() {
            return this.audioFiles;
        }

        public void setAudioFiles(List<String> audioFiles) {
            this.audioFiles = audioFiles;
        }

        public String getCallFlowId() {
            return this.callFlowId;
        }

        public void setCallFlowId(String callFlowId) {
            this.callFlowId = callFlowId;
        }

        @JsonIgnore
        public boolean badRequest() {
            return StringUtils.isBlank(this.phoneNumber) || StringUtils.isBlank(this.callFlowId)
                    || this.audioFiles == null || this.audioFiles.isEmpty();
        }
    }

    public static class MessageRequest {
        private String phoneNumber;
        private String message;
        private String smsLanguage;

        public String getPhoneNumber() {
            return this.phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSmsLanguage() {
            return this.smsLanguage;
        }

        public SmsLanguage getSmsLanguageEnum() {
            return SmsLanguage.find(this.smsLanguage);
        }

        public void setSmsLanguage(String smsLanguage) {
            this.smsLanguage = smsLanguage;
        }

        @JsonIgnore
        public boolean badRequest() {
            return StringUtils.isBlank(this.phoneNumber) || StringUtils.isBlank(this.message)
                    || SmsLanguage.find(this.smsLanguage) == null;
        }
    }
} 

package com.followapp.core.api;

import com.followapp.core.data.CallHistoryDao;
import com.followapp.core.model.CallResult;
import com.followapp.core.model.CallStatus;
import com.followapp.core.model.ImiMobileResponse;
import com.followapp.core.model.ScheduleDetail;
import com.followapp.core.model.ScheduleRun;
import com.followapp.core.services.CallingService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;

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

    @RequestMapping(value = "test", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> testSomething() {
        return new ResponseEntity<String>("Hello\nService is up!", HttpStatus.OK);
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

        return new ResponseEntity<String>("Call to " + scheduleDetail.getRecipientMobileNumber() + " has been initiated", HttpStatus.OK);
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

    private CallStatus getStatus(String response) {
        if (StringUtils.equals(response, "1")) {
            return CallStatus.PRESCRIPTION_TAKEN;
        }
        return CallStatus.PRESCRIPTION_NOT_TAKEN;
    }
} 

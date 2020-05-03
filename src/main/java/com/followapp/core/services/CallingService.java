package com.followapp.core.services;


import com.followapp.core.imimobile.ImiMobileApiException;
import com.followapp.core.model.ScheduleDetail;
import com.followapp.core.model.ScheduleRun;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class CallingService {

    private static final Logger LOG = LoggerFactory.getLogger(CallingService.class);

    private final ICallingServiceApi imiMobileApi;

    @Value("{app.imi.call.callBackEndpoint}")
    private String callBackEndpoint;

    @Value("app.imi.call.callFlowId")
    private String callFlowId;

    @Autowired
    public CallingService(ICallingServiceApi imiMobileApi) {
        this.imiMobileApi = imiMobileApi;
    }

    public ScheduleRun callUser(ScheduleDetail scheduleDetail) {
        String phoneNumber = scheduleDetail.getRecipientMobileNumber();
        Objects.requireNonNull(phoneNumber, "Phone number to be called cannot be null");
        if (phoneNumber.length() < 10) {
            throw new IllegalArgumentException(
                    "Phone number is not in the required format. It should be 11 digits long. "
                            + "Try appending a leading 0 to the phone number, in case it is only 10 digits long.");
        }
        if (phoneNumber.length() == 10) {
            phoneNumber = "0" + phoneNumber;
        }
        List<String> audioFiles = getAudioFiles(scheduleDetail);
        ScheduleRun scheduleRun;
        try {
            String ivrRequestId = imiMobileApi.call(phoneNumber, audioFiles, CallingServiceApiAttributes.aCallingServiceApiAttributes()
                    .callFlowId(callFlowId).callBackEndpoint(callBackEndpoint).build());
            scheduleRun = ScheduleRun.aSuccessfulScheduleRun(scheduleDetail, ivrRequestId);
        } catch (ImiMobileApiException exception) {
            LOG.error(String.format("Error while calling %s ", phoneNumber), exception);
            scheduleRun = ScheduleRun.aFailedScheduleRun(scheduleDetail);
        }
        LOG.info("Returning call result: {}", scheduleRun);
        return scheduleRun;
    }

    private List<String> getAudioFiles(ScheduleDetail scheduleDetail) {
        return Arrays.asList(getFileNameAudioStorageFormat(scheduleDetail.getAudioFileRelativePath()));
    }

    private String getFileNameAudioStorageFormat(String audioFileName) {
        return StringUtils.lowerCase(StringUtils.replace(audioFileName, " ", "_"));
    }
}

package com.followapp.core.services;

import com.followapp.core.imimobile.ImiMobileApiException;
import com.followapp.core.model.ScheduleDetail;
import com.followapp.core.model.ScheduleRun;
import com.followapp.core.model.SmsLanguage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessagingService {

    private static final Logger LOG = LoggerFactory.getLogger(CallingService.class);

    private final IMessagingServiceApi messagingServiceApi;

    @Autowired
    public MessagingService(IMessagingServiceApi messagingServiceApi) {
        this.messagingServiceApi = messagingServiceApi;
    }

    public ScheduleRun messageUser(ScheduleDetail scheduleDetail) {
        ScheduleRun scheduleRun;
        try {
            String ivrRequestId = null;
            if (scheduleDetail.getSmsLanguageEnum() == SmsLanguage.ENGLISH) {
                ivrRequestId = this.messagingServiceApi.sendMessage(scheduleDetail.getRecipientMobileNumber(),
                        scheduleDetail.getSmsText());
            } else {
                ivrRequestId = this.messagingServiceApi.sendMessageUnicode(scheduleDetail.getRecipientMobileNumber(),
                        scheduleDetail.getSmsText());
            }
            scheduleRun = ScheduleRun.aSuccessfulScheduleRun(scheduleDetail, ivrRequestId);
        } catch (ImiMobileApiException exception) {
            LOG.error(String.format("Error while calling %s ", scheduleDetail.getRecipientMobileNumber()), exception);
            scheduleRun = ScheduleRun.aFailedScheduleRun(scheduleDetail);
        }
        LOG.info("Returning schedule run: {}", scheduleRun);
        return scheduleRun;
    }
}

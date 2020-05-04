package com.followapp.core.batch;

import com.followapp.core.model.ActionType;
import com.followapp.core.model.ScheduleDetail;
import com.followapp.core.model.ScheduleRun;
import com.followapp.core.services.CallingService;
import com.followapp.core.services.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IvrProcessor implements ItemProcessor<ScheduleDetail, ScheduleRun> {

    private static final Logger LOG = LoggerFactory.getLogger(IvrProcessor.class);

    @Autowired
    private CallingService callingService;

    @Autowired
    private MessagingService messagingService;

    @Value("${app.calls.mocked:false}")
    private Boolean isCallMocked;

    public ScheduleRun process(ScheduleDetail scheduleDetail) throws Exception {
        if (this.isCallMocked) {
            LOG.info("Call is being mocked. No call will actually be made");
            return ScheduleRun.aSuccessfulScheduleRun(scheduleDetail, UUID.randomUUID().toString());
        }
        if (scheduleDetail.getActionType() == ActionType.CALL) {
            return this.callingService.callUser(scheduleDetail);
        } else {
            return this.messagingService.messageUser(scheduleDetail);
        }
    }
}

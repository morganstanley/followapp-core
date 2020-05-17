package com.followapp.core.services;

import com.followapp.core.data.CallHistoryDao;
import com.followapp.core.model.ActionType;
import com.followapp.core.model.ScheduleRunStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class SmsDeliveryStatusService {

    @Resource
    private CallHistoryDao callHistoryDao;

    @Resource
    private IMessagingServiceApi messagingServiceApi;

    public void process(ScheduleRunStatus scheduleRunStatus, ActionType actionType, LocalDateTime runTime) {
        List<String> requestIds = this.callHistoryDao.requestWithStatus(scheduleRunStatus.name(), actionType.name(), runTime);
        requestIds.stream().forEach(requestId -> {
            ScheduleRunStatus status = this.messagingServiceApi.getStatus(requestId);
            this.callHistoryDao.updateMessageStatus(requestId, status.name());
        });
    }
}

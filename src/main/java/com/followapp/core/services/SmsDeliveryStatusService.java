package com.followapp.core.services;

import com.followapp.core.data.CallHistoryDao;
import com.followapp.core.model.ActionType;
import com.followapp.core.model.ScheduleRunStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SmsDeliveryStatusService {
    private static final Logger LOG = LoggerFactory.getLogger(SmsDeliveryStatusService.class);

    @Resource
    private CallHistoryDao callHistoryDao;

    @Resource
    private IMessagingServiceApi messagingServiceApi;

    public void process(ActionType actionType, LocalDateTime runTime, ScheduleRunStatus... scheduleRunStatus) {
        List<String> requestIds = this.callHistoryDao.requestWithStatus(Arrays.asList(scheduleRunStatus).stream()
                .map(ScheduleRunStatus::name).collect(Collectors.toList()), actionType.name(), runTime);
        if (requestIds == null || requestIds.size() == 0) {
            LOG.info("Fetched 0 rows with status {}", Arrays.asList(scheduleRunStatus));
        }
        LOG.info("Fetched {} requests with status {} actionType {} runTime {}",
                requestIds.size(), Arrays.asList(scheduleRunStatus), actionType, runTime);
        requestIds.stream().forEach(requestId -> {
            ScheduleRunStatus status = this.messagingServiceApi.getStatus(requestId);
            this.callHistoryDao.updateMessageStatus(requestId, status.name());
        });
    }
}

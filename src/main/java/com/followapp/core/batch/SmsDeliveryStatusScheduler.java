package com.followapp.core.batch;

import com.followapp.core.model.ActionType;
import com.followapp.core.model.ScheduleRunStatus;
import com.followapp.core.services.SmsDeliveryStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Configuration
@EnableScheduling
public class SmsDeliveryStatusScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(SmsDeliveryStatusScheduler.class);

    @Resource
    private SmsDeliveryStatusService smsDeliveryStatusService;

    @Scheduled(cron = "0 ${app.scheduler.batchTriggerTime} 6 * * *")
    public void noResponseRequestCheck() {
        LOG.info("Schedule run for sms delivery status {}", LocalDateTime.now());
        this.smsDeliveryStatusService.process(ActionType.SMS, LocalDate.now().atStartOfDay(),
                ScheduleRunStatus.IVR_REQUESTED, ScheduleRunStatus.PARTIALLY_DELIVERED, ScheduleRunStatus.SUBMITTED);
    }
}

package com.followapp.core.services;

import com.followapp.core.model.ScheduleRunStatus;

import java.io.IOException;

public interface IMessagingServiceApi {
    String sendMessage(String phoneNumber, String messageText);

    String sendMessageUnicode(String phoneNumber, String messageText);

    ScheduleRunStatus getStatus(String requestId);
}

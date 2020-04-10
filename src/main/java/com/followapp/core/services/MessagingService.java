package com.followapp.core.services;

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

    public void messageUser(String phoneNumber, String message) {
        this.messagingServiceApi.sendMessage(phoneNumber, message);
    }
}

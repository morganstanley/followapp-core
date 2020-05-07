package com.followapp.core;

import org.springframework.boot.SpringApplication;

import java.net.URLEncoder;
import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        String strMainContent = String.format(
                "{\"outboundSMSMessageRequest\":{" +
                        "\"address\":[\"%s\"]," +
                        "\"senderAddress\":\"%s\"," +
                        "\"outboundSMSTextMessage\":{\"message\":\"%s\"}," +
                        "\"messageType\":\"0\"," +
                        "\"clientCorrelator\":\"%s\"," +
                        "\"senderName\":\"%s\"}" +
                                          "}", formatPhoneNumber("8108140510"), formatPhoneNumber("7405280110"), "Test", UUID.randomUUID(), "Raju");
        System.out.println(strMainContent);
    }

    private static String formatPhoneNumber(String phoneNumber) {
        return (String.format("tel:%s", phoneNumber));
    }
}

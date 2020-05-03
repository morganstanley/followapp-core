package com.followapp.core.model;

import java.util.Arrays;

public enum ScheduleRunStatus {
    IVR_REQUESTED,
    IVR_FAILURE,

    HUNG_UP,
    CALL_NOT_RECIEVED,
    UNAUTHORIZED_CALL,
    CALL_FAILED,
    UNKNOWN,

    SMS_DELIVERED,
    SMS_DND;

    public static ScheduleRunStatus find(String value) {
        return Arrays.asList(values()).stream()
                .filter(scheduleRunStatus -> scheduleRunStatus.name().equalsIgnoreCase(value)).findFirst().orElse(UNKNOWN);
    }
}

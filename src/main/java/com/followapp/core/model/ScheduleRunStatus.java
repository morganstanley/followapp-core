package com.followapp.core.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum ScheduleRunStatus {
    IVR_REQUESTED,
    IVR_FAILURE,

    HUNG_UP,
    CALL_NOT_RECIEVED,
    UNAUTHORIZED_CALL,
    CALL_FAILED,
    UNKNOWN,

    SMS_DELIVERED("Delivered"),
    SMS_DND;

    private final Set<String> alias;

    ScheduleRunStatus(String... status) {
        this.alias = new HashSet<>();
        Arrays.asList(status).stream().map(StringUtils::upperCase).forEach(this.alias::add);
        this.alias.add(StringUtils.upperCase(this.name()));
    }

    public static ScheduleRunStatus find(String value) {
        return Arrays.asList(values()).stream()
                .filter(scheduleRunStatus -> scheduleRunStatus.alias.contains(StringUtils.upperCase(value))).findFirst().orElse(UNKNOWN);
    }
}

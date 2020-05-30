package com.followapp.core.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
    PARTIALLY_DELIVERED,
    SMS_DND,
    SUBMITTED;


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

    public static ScheduleRunStatus aggregate(List<ScheduleRunStatus> scheduleRunStatuses) {
        if (scheduleRunStatuses.stream().allMatch(scheduleRunStatus -> scheduleRunStatus == ScheduleRunStatus.SMS_DELIVERED)) {
            return ScheduleRunStatus.SMS_DELIVERED;
        }
        if (scheduleRunStatuses.stream().allMatch(scheduleRunStatus -> scheduleRunStatus == ScheduleRunStatus.SUBMITTED)) {
            return ScheduleRunStatus.SUBMITTED;
        }
        if (scheduleRunStatuses.stream().anyMatch(scheduleRunStatus -> scheduleRunStatus == ScheduleRunStatus.SUBMITTED) &&
                scheduleRunStatuses.stream().anyMatch(scheduleRunStatus -> scheduleRunStatus == ScheduleRunStatus.SMS_DELIVERED)) {
            return ScheduleRunStatus.PARTIALLY_DELIVERED;
        }
        return ScheduleRunStatus.UNKNOWN;
    }
}

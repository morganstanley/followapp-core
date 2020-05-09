package com.followapp.core.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class ScheduleRun {

    private Integer scheduleId;
    private Integer recipientId;
    private String ivrRequestId;
    private LocalDateTime runDateTime;
    private LocalDateTime updateDateTime;
    private ScheduleRunStatus status;
    private Integer callDuration;

    public ScheduleRun() {
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public String getIvrRequestId() {
        return ivrRequestId;
    }

    public void setIvrRequestId(String ivrRequestId) {
        this.ivrRequestId = ivrRequestId;
    }

    public LocalDateTime getRunDateTime() {
        return runDateTime;
    }

    public void setRunDateTime(LocalDateTime runDateTime) {
        this.runDateTime = runDateTime;
    }


    public void setStatus(ScheduleRunStatus status) {
        this.status = status;
    }

    public ScheduleRunStatus getStatus() {
        return status;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public Integer getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(Integer callDuration) {
        this.callDuration = callDuration;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ScheduleRun{");
        sb.append("scheduleId=").append(scheduleId);
        sb.append(", recipientId=").append(recipientId);
        sb.append(", ivrRequestId='").append(ivrRequestId).append('\'');
        sb.append(", runDateTime=").append(runDateTime);
        sb.append(", status='").append(status).append('\'');
        sb.append(", updateDateTime=").append(updateDateTime);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleRun that = (ScheduleRun) o;
        return Objects.equals(scheduleId, that.scheduleId) &&
                Objects.equals(recipientId, that.recipientId) &&
                Objects.equals(ivrRequestId, that.ivrRequestId) &&
                Objects.equals(runDateTime, that.runDateTime) &&
                Objects.equals(updateDateTime, that.updateDateTime) &&
                status == that.status &&
                Objects.equals(callDuration, that.callDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleId, recipientId, ivrRequestId, runDateTime, updateDateTime, status, callDuration);
    }

    public static ScheduleRun aSuccessfulScheduleRun(ScheduleDetail scheduleDetail, String ivrRequestId) {
        ScheduleRun scheduleRun = new ScheduleRun();
        scheduleRun.scheduleId = scheduleDetail.getScheduleId();
        scheduleRun.recipientId = scheduleDetail.getRecipientId();
        scheduleRun.ivrRequestId = ivrRequestId;
        scheduleRun.runDateTime = LocalDateTime.now();
        scheduleRun.updateDateTime = LocalDateTime.now();
        scheduleRun.status = ScheduleRunStatus.IVR_REQUESTED;
        return scheduleRun;
    }

    public static ScheduleRun aFailedScheduleRun(ScheduleDetail scheduleDetail) {
        ScheduleRun scheduleRun = new ScheduleRun();
        scheduleRun.scheduleId = scheduleDetail.getScheduleId();
        scheduleRun.recipientId = scheduleDetail.getRecipientId();
        scheduleRun.runDateTime = LocalDateTime.now();
        scheduleRun.updateDateTime = LocalDateTime.now();
        scheduleRun.status = ScheduleRunStatus.IVR_FAILURE;
        return scheduleRun;
    }
}

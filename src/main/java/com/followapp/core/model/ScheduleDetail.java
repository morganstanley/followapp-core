package com.followapp.core.model;

public class ScheduleDetail {

    private Integer scheduleId;
    private Integer groupId;
    private ActionType actionType;

    private Integer recipientId;
    private String recipientName;
    private String recipientMobileNumber;
    private String recipientZone;
    private String recipientGender;

    private Integer audioFileId;
    private Integer audioFileName;
    private String audioFileRelativePath;
    private String audioMetaData;

    private Integer smsContentId;
    private String smsText;
    private String smsLanguage;
    private String smsMetaData;

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientMobileNumber() {
        return recipientMobileNumber;
    }

    public void setRecipientMobileNumber(String recipientMobileNumber) {
        this.recipientMobileNumber = recipientMobileNumber;
    }

    public String getRecipientZone() {
        return recipientZone;
    }

    public void setRecipientZone(String recipientZone) {
        this.recipientZone = recipientZone;
    }

    public String getRecipientGender() {
        return recipientGender;
    }

    public void setRecipientGender(String recipientGender) {
        this.recipientGender = recipientGender;
    }

    public Integer getAudioFileId() {
        return audioFileId;
    }

    public void setAudioFileId(Integer audioFileId) {
        this.audioFileId = audioFileId;
    }

    public Integer getAudioFileName() {
        return audioFileName;
    }

    public void setAudioFileName(Integer audioFileName) {
        this.audioFileName = audioFileName;
    }

    public String getAudioFileRelativePath() {
        return audioFileRelativePath;
    }

    public void setAudioFileRelativePath(String audioFileRelativePath) {
        this.audioFileRelativePath = audioFileRelativePath;
    }

    public String getAudioMetaData() {
        return audioMetaData;
    }

    public void setAudioMetaData(String audioMetaData) {
        this.audioMetaData = audioMetaData;
    }

    public Integer getSmsContentId() {
        return smsContentId;
    }

    public void setSmsContentId(Integer smsContentId) {
        this.smsContentId = smsContentId;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public String getSmsLanguage() {
        return smsLanguage;
    }

    public void setSmsLanguage(String smsLanguage) {
        this.smsLanguage = smsLanguage;
    }

    public String getSmsMetaData() {
        return smsMetaData;
    }

    public void setSmsMetaData(String smsMetaData) {
        this.smsMetaData = smsMetaData;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ScheduleDetail{");
        sb.append("scheduleId=").append(scheduleId);
        sb.append(", groupId=").append(groupId);
        sb.append(", actionType=").append(actionType);
        sb.append(", recipientId=").append(recipientId);
        sb.append(", recipientName='").append(recipientName).append('\'');
        sb.append(", recipientMobileNumber='").append(recipientMobileNumber).append('\'');
        sb.append(", recipientZone='").append(recipientZone).append('\'');
        sb.append(", recipientGender='").append(recipientGender).append('\'');
        sb.append(", audioFileId=").append(audioFileId);
        sb.append(", audioFileName=").append(audioFileName);
        sb.append(", audioFileRelativePath='").append(audioFileRelativePath).append('\'');
        sb.append(", audioMetaData='").append(audioMetaData).append('\'');
        sb.append(", smsContentId=").append(smsContentId);
        sb.append(", smsText='").append(smsText).append('\'');
        sb.append(", smsLanguage='").append(smsLanguage).append('\'');
        sb.append(", smsMetaData='").append(smsMetaData).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

package com.followapp.core.model;

import java.time.LocalDateTime;

public class Schedule {
    private Integer scheduleId;
    private Integer groupId;
    private String groupName;
    private String description;
    private LocalDateTime executeTime;
    private ActionType actionType;
    private Integer audioFileId;
    private Integer smsContentId;
    private String smsText;
    private String smsLanguage;
    private String audioFileName;
    private String status;
    private Boolean deleteFlag;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(LocalDateTime executeTime) {
        this.executeTime = executeTime;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Integer getAudioFileId() {
        return audioFileId;
    }
    
    public void setAudioFileId(Integer audioFileId) {
        this.audioFileId = audioFileId;
    }

    public Integer getSmsContentId() {
        return smsContentId;
    }

    public void setSmsContentId(Integer smsContentId) {
        this.smsContentId = smsContentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Schedule{");
        sb.append("scheduleId=").append(scheduleId);
        sb.append(", groupId=").append(groupId);
        sb.append(", groupName=").append(groupName);
        sb.append(", description='").append(description).append('\'');
        sb.append(", executeTime=").append(executeTime);
        sb.append(", actionType=").append(actionType);
        sb.append(", audioFileId=").append(audioFileId);
        sb.append(", audioFileName=").append(audioFileName);
        sb.append(", smsContentId=").append(smsContentId);
        sb.append(", smsText=").append(smsText);
        sb.append(", smsLanguage=").append(smsLanguage);
        sb.append(", status='").append(status).append('\'');
        sb.append(", deleteFlag=").append(deleteFlag);
        sb.append(", createdDateTime=").append(createdDateTime);
        sb.append(", updatedDateTime=").append(updatedDateTime);
        sb.append('}');
        return sb.toString();
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

	public String getAudioFileName() {
		return audioFileName;
	}

	public void setAudioFileName(String audioFileName) {
		this.audioFileName = audioFileName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}

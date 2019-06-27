package com.followapp.core.model;

import javax.xml.bind.annotation.XmlElement;

public class EvtInfo {

    private String acceptedOn;

    private String answeredOn;

    private String totalPulses;

    private ExternalParams externalParams;

    private String releasedOn;

    private String dropReason;

    private String tid;

    private String dropType;

    private String offeredOn;

    private String callDuration;

    private String callResult;

    private String dropDescription;

    @XmlElement(name = "accepted-on")
    public String getAcceptedOn() {
        return acceptedOn;
    }

    public void setAcceptedOn(String acceptedOn) {
        this.acceptedOn = acceptedOn;
    }

    @XmlElement(name = "answered-on")
    public String getAnsweredOn() {
        return answeredOn;
    }

    public void setAnsweredOn(String answeredOn) {
        this.answeredOn = answeredOn;
    }

    @XmlElement(name = "total-pulses")
    public String getTotalPulses() {
        return totalPulses;
    }

    public void setTotalPulses(String totalPulses) {
        this.totalPulses = totalPulses;
    }

    @XmlElement(name = "externalParams")
    public ExternalParams getExternalParams() {
        return externalParams;
    }

    public void setExternalParams(ExternalParams externalParams) {
        this.externalParams = externalParams;
    }

    @XmlElement(name = "released-on")
    public String getReleasedOn() {
        return releasedOn;
    }

    public void setReleasedOn(String releasedOn) {
        this.releasedOn = releasedOn;
    }

    @XmlElement(name = "drop-reason")
    public String getDropReason() {
        return dropReason;
    }

    public void setDropReason(String dropReason) {
        this.dropReason = dropReason;
    }

    @XmlElement
    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    @XmlElement(name = "drop-type")
    public String getDropType() {
        return dropType;
    }

    public void setDropType(String dropType) {
        this.dropType = dropType;
    }

    @XmlElement(name = "offered-on")
    public String getOfferedOn() {
        return offeredOn;
    }

    public void setOfferedOn(String offeredOn) {
        this.offeredOn = offeredOn;
    }

    @XmlElement(name = "call-duration")
    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    @XmlElement(name = "callresult")
    public String getCallResult() {
        return callResult;
    }

    public void setCallResult(String callResult) {
        this.callResult = callResult;
    }

    @XmlElement(name = "drop-description")
    public String getDropDescription() {
        return dropDescription;
    }

    public void setDropDescription(String dropDescription) {
        this.dropDescription = dropDescription;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EvtInfo [acceptedOn=").append(acceptedOn).append(", answeredOn=").append(answeredOn)
                .append(", totalPulses=").append(totalPulses).append(", externalParams=").append(externalParams)
                .append(", releasedOn=").append(releasedOn).append(", dropReason=").append(dropReason).append(", tid=")
                .append(tid).append(", dropType=").append(dropType).append(", offeredOn=").append(offeredOn)
                .append(", callDuration=").append(callDuration).append(", callResult=").append(callResult)
                .append(", dropDescription=").append(dropDescription).append("]");
        return builder.toString();
    }
}

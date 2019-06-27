package com.followapp.core.model;

import javax.xml.bind.annotation.XmlElement;

public class EvtNotification {

    private EvtInfo evtInfo;

    @XmlElement(name = "evt-info")
    public EvtInfo getEvtInfo() {
        return evtInfo;
    }

    public void setEvtInfo(EvtInfo evtInfo) {
        this.evtInfo = evtInfo;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EvtNotification [evtInfo=").append(this.evtInfo).append("]");
        return builder.toString();
    }
}

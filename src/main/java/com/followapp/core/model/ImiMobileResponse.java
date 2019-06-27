package com.followapp.core.model;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class ImiMobileResponse {

    @XmlElement(name = "evt-notification")
    private EvtNotification evtNotification;

    public EvtNotification getEvt() {
        return this.evtNotification;
    }

    public void setEvt(EvtNotification evt) {
        this.evtNotification = evt;
    }

    public String getUuid() {
        return this.evtNotification.getEvtInfo().getExternalParams().getEsbTransId();
    }

    public String getInput() {
        return this.evtNotification.getEvtInfo().getExternalParams().getDtmf();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ImiMobileResponse [evtNotification=").append(this.evtNotification).append("]");
        return builder.toString();
    }
}

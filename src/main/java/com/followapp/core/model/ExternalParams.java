package com.followapp.core.model;

import javax.xml.bind.annotation.XmlElement;

public class ExternalParams {

    private String esbTransId;

    private String errorCode;

    private String dtmf;

    @XmlElement(name = "_esb_trans_id")
    public String getEsbTransId() {
        return esbTransId;
    }

    public void setEsbTransId(String esbTransId) {
        this.esbTransId = esbTransId;
    }

    @XmlElement(name = "_errorcode")
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @XmlElement(name = "_dtmf")
    public String getDtmf() {
        return dtmf;
    }

    public void setDtmf(String dtmf) {
        this.dtmf = dtmf;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ExternalParams [esbTransId=").append(esbTransId).append(", errorCode=").append(errorCode)
                .append(", dtmf=").append(dtmf).append("]");
        return builder.toString();
    }
}

package com.followapp.core.model.sms;

import java.time.LocalDateTime;

public class DeliveryInfo {
    private String address;

    private String errorCode;

    private LocalDateTime deliveryDate;

    private String deliveryStatus;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DeliveryInfo{");
        sb.append("address='").append(address).append('\'');
        sb.append(", errorCode='").append(errorCode).append('\'');
        sb.append(", deliveryDate=").append(deliveryDate);
        sb.append(", deliveryStatus='").append(deliveryStatus).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

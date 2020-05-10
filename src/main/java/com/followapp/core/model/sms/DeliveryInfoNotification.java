package com.followapp.core.model.sms;

public class DeliveryInfoNotification {
    private String message;

    private String senderAddress;

    private String clientCorrelator;

    private DeliveryInfo deliveryInfo;

    @com.fasterxml.jackson.annotation.JsonProperty(value = "RequestId")
    private String requestId;

    private String callbackData;

    private String serviceName;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getClientCorrelator() {
        return clientCorrelator;
    }

    public void setClientCorrelator(String clientCorrelator) {
        this.clientCorrelator = clientCorrelator;
    }

    public DeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCallbackData() {
        return callbackData;
    }

    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DeliveryInfoNotification{");
        sb.append("message='").append(message).append('\'');
        sb.append(", senderAddress='").append(senderAddress).append('\'');
        sb.append(", clientCorrelator='").append(clientCorrelator).append('\'');
        sb.append(", deliveryInfo=").append(deliveryInfo);
        sb.append(", RequestId='").append(requestId).append('\'');
        sb.append(", callbackData='").append(callbackData).append('\'');
        sb.append(", serviceName='").append(serviceName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}


package com.followapp.core.model.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(builder = DeliveryInfoNotification.DeliveryInfoNotificationBuilder.class)
public class DeliveryInfoNotification {
    private final String message;
    private final String senderAddress;
    private final String clientCorrelator;
    private final List<DeliveryInfo> deliveryInfo;
    private final String requestId;
    private final String callbackData;
    private final String serviceName;

    private DeliveryInfoNotification(DeliveryInfoNotificationBuilder builder) {
        this.message = builder.message;
        this.senderAddress = builder.senderAddress;
        this.clientCorrelator = builder.clientCorrelator;
        this.deliveryInfo = builder.deliveryInfo;
        this.requestId = builder.requestId;
        this.callbackData = builder.callbackData;
        this.serviceName = builder.serviceName;
    }

    public String message() {
        return this.message;
    }

    public String senderAddress() {
        return this.senderAddress;
    }

    public String clientCorrelator() {
        return this.clientCorrelator;
    }

    public List<DeliveryInfo> deliveryInfo() {
        return this.deliveryInfo;
    }

    @JsonProperty(value = "RequestId")
    public String requestId() {
        return this.requestId;
    }

    public String callbackData() {
        return this.callbackData;
    }

    public String serviceName() {
        return this.serviceName;
    }

    public static DeliveryInfoNotificationBuilder aDeliveryInfoNotification() {
        return new DeliveryInfoNotificationBuilder();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DeliveryInfoNotification{");
        sb.append("message='").append(this.message).append('\'');
        sb.append(", senderAddress='").append(this.senderAddress).append('\'');
        sb.append(", clientCorrelator='").append(this.clientCorrelator).append('\'');
        sb.append(", deliveryInfo=").append(this.deliveryInfo);
        sb.append(", RequestId='").append(this.requestId).append('\'');
        sb.append(", callbackData='").append(this.callbackData).append('\'');
        sb.append(", serviceName='").append(this.serviceName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @JsonPOJOBuilder
    public static class DeliveryInfoNotificationBuilder {
        private String message;
        private String senderAddress;
        private String clientCorrelator;
        private List<DeliveryInfo> deliveryInfo;
        private String requestId;
        private String callbackData;
        private String serviceName;

        private DeliveryInfoNotificationBuilder() {
            this.deliveryInfo = new ArrayList<>();
        }

        @JsonSetter
        public DeliveryInfoNotificationBuilder message(String message) {
            this.message = message;
            return this;
        }

        @JsonSetter
        public DeliveryInfoNotificationBuilder senderAddress(String senderAddress) {
            this.senderAddress = senderAddress;
            return this;
        }

        @JsonSetter
        public DeliveryInfoNotificationBuilder clientCorrelator(String clientCorrelator) {
            this.clientCorrelator = clientCorrelator;
            return this;
        }

        @JsonSetter
        public DeliveryInfoNotificationBuilder deliveryInfo(List<DeliveryInfo> deliveryInfo) {
            if (deliveryInfo != null) {
                this.deliveryInfo.addAll(deliveryInfo);
            }
            return this;
        }

        @JsonSetter("RequestId")
        public DeliveryInfoNotificationBuilder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        @JsonSetter
        public DeliveryInfoNotificationBuilder callbackData(String callbackData) {
            this.callbackData = callbackData;
            return this;
        }

        @JsonSetter
        public DeliveryInfoNotificationBuilder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public DeliveryInfoNotification build() {
            return new DeliveryInfoNotification(this);
        }
    }
}


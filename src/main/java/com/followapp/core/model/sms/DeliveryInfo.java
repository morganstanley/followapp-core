package com.followapp.core.model.sms;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = DeliveryInfo.DeliveryInfoBuilder.class)
public class DeliveryInfo {
    private final String address;
    private final String errorCode;
    private final String deliveryDate;
    private final String deliveryStatus;

    private DeliveryInfo(DeliveryInfoBuilder builder) {
        this.address = builder.address;
        this.errorCode = builder.errorCode;
        this.deliveryDate = builder.deliveryDate;
        this.deliveryStatus = builder.deliveryStatus;
    }

    public String address() {
        return this.address;
    }

    public String errorCode() {
        return this.errorCode;
    }

    public String deliveryDate() {
        return this.deliveryDate;
    }

    public String deliveryStatus() {
        return this.deliveryStatus;
    }

    public static DeliveryInfoBuilder aDeliveryInfo() {
        return new DeliveryInfoBuilder();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DeliveryInfo{");
        sb.append("address='").append(this.address).append('\'');
        sb.append(", errorCode='").append(this.errorCode).append('\'');
        sb.append(", deliveryDate=").append(this.deliveryDate);
        sb.append(", deliveryStatus='").append(this.deliveryStatus).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @JsonPOJOBuilder
    public static class DeliveryInfoBuilder {
        private String address;
        private String errorCode;
        private String deliveryDate;
        private String deliveryStatus;

        private DeliveryInfoBuilder() {
        }

        @JsonSetter
        public DeliveryInfoBuilder address(String address) {
            this.address = address;
            return this;
        }

        @JsonSetter
        public DeliveryInfoBuilder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        @JsonSetter
        public DeliveryInfoBuilder deliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
            return this;
        }

        @JsonSetter
        public DeliveryInfoBuilder deliveryStatus(String deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
            return this;
        }

        public DeliveryInfo build() {
            return new DeliveryInfo(this);
        }
    }
}

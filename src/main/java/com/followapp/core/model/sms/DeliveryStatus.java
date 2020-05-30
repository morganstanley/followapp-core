package com.followapp.core.model.sms;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = DeliveryStatus.DeliveryStatusBuilder.class)
public class DeliveryStatus {

    private final DeliveryInfoNotification deliveryInfoNotification;

    private DeliveryStatus(DeliveryStatusBuilder builder) {
        this.deliveryInfoNotification = builder.deliveryInfoNotification;
    }

    public DeliveryInfoNotification deliveryInfoNotification() {
        return this.deliveryInfoNotification;
    }

    public static DeliveryStatusBuilder aDeliveryStatus() {
        return new DeliveryStatusBuilder();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DeliveryStatus{");
        sb.append("deliveryInfoNotification=").append(deliveryInfoNotification);
        sb.append('}');
        return sb.toString();
    }

    @JsonPOJOBuilder
    public static class DeliveryStatusBuilder {
        private DeliveryInfoNotification deliveryInfoNotification;

        private DeliveryStatusBuilder() {
        }

        @JsonSetter
        public DeliveryStatusBuilder deliveryInfoNotification(DeliveryInfoNotification deliveryInfoNotification) {
            this.deliveryInfoNotification = deliveryInfoNotification;
            return this;
        }

        public DeliveryStatus build() {
            return new DeliveryStatus(this);
        }
    }
}

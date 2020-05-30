package com.followapp.core.model.sms;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = SmsDeliveryQueryResponse.SmsDeliveryQueryResponseBuilder.class)
public class SmsDeliveryQueryResponse {
    private final DeliveryInfoList deliveryInfoList;

    private SmsDeliveryQueryResponse(SmsDeliveryQueryResponseBuilder builder) {
        this.deliveryInfoList = builder.deliveryInfoList;
    }

    public DeliveryInfoList deliveryInfoList() {
        return this.deliveryInfoList;
    }

    public static SmsDeliveryQueryResponseBuilder aSmsDeliveryQueryResponse() {
        return new SmsDeliveryQueryResponseBuilder();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SmsDeliveryQueryResponse{");
        sb.append("deliveryInfoList=").append(deliveryInfoList);
        sb.append('}');
        return sb.toString();
    }

    @JsonPOJOBuilder
    public static class SmsDeliveryQueryResponseBuilder {
        private DeliveryInfoList deliveryInfoList;

        @JsonSetter
        public SmsDeliveryQueryResponseBuilder deliveryInfoList(DeliveryInfoList deliveryInfoList) {
            this.deliveryInfoList = deliveryInfoList;
            return this;
        }

        public SmsDeliveryQueryResponse build() {
            return new SmsDeliveryQueryResponse(this);
        }
    }
}

package com.followapp.core.model.sms;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(builder = DeliveryInfoList.DeliveryInfoListBuilder.class)
public class DeliveryInfoList {
    private final List<DeliveryInfo> deliveryInfo;
    private final String resourceURL;

    private DeliveryInfoList(DeliveryInfoListBuilder builder) {
        this.deliveryInfo = builder.deliveryInfo;
        this.resourceURL = builder.resourceURL;
    }

    public List<DeliveryInfo> deliveryInfo() {
        return this.deliveryInfo;
    }

    public String resourceURL() {
        return this.resourceURL;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DeliveryInfoList{");
        sb.append("deliveryInfo=").append(this.deliveryInfo);
        sb.append(", resourceURL='").append(this.resourceURL).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public DeliveryInfoListBuilder aDeliveryInfoListBuilder() {
        return new DeliveryInfoListBuilder();
    }

    @JsonPOJOBuilder
    public static class DeliveryInfoListBuilder {
        private List<DeliveryInfo> deliveryInfo;
        private String resourceURL;

        private DeliveryInfoListBuilder() {
            this.deliveryInfo = new ArrayList<>();
        }

        @JsonSetter
        public DeliveryInfoListBuilder deliveryInfo(List<DeliveryInfo> deliveryInfo) {
            if (deliveryInfo != null) {
                this.deliveryInfo.addAll(deliveryInfo);
            }
            return this;
        }

        @JsonSetter
        public DeliveryInfoListBuilder resourceURL(String resourceURL) {
            this.resourceURL = resourceURL;
            return this;
        }

        public DeliveryInfoList build() {
            return new DeliveryInfoList(this);
        }
    }
}

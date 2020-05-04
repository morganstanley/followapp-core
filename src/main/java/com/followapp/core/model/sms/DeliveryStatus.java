package com.followapp.core.model.sms;

public class DeliveryStatus {

    private DeliveryInfoNotification deliveryInfoNotification;

    public DeliveryInfoNotification getDeliveryInfoNotification() {
        return deliveryInfoNotification;
    }

    public void setDeliveryInfoNotification(DeliveryInfoNotification deliveryInfoNotification) {
        this.deliveryInfoNotification = deliveryInfoNotification;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DeliveryStatus{");
        sb.append("deliveryInfoNotification=").append(deliveryInfoNotification);
        sb.append('}');
        return sb.toString();
    }
}

package com.followapp.core.services;

public class CallingServiceApiAttributes {
    private final String callFlowId;
    private final String callBackEndpoint;

    private CallingServiceApiAttributes(CallingServiceApiAttributesBuilder builder) {
        this.callFlowId = builder.callFlowId;
        this.callBackEndpoint = builder.callBackEndpoint;
    }

    public String getCallFlowId() {
        return this.callFlowId;
    }

    public String getCallBackEndpoint() {
        return this.callBackEndpoint;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CallingServiceApiAttributes{");
        sb.append("callFlowId='").append(this.callFlowId).append('\'');
        sb.append(", callBackEndpoint='").append(this.callBackEndpoint).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static CallingServiceApiAttributesBuilder aCallingServiceApiAttributes() {
        return new CallingServiceApiAttributesBuilder();
    }

    public static class CallingServiceApiAttributesBuilder {
        private String callFlowId;
        private String callBackEndpoint;

        private CallingServiceApiAttributesBuilder() {
        }

        public CallingServiceApiAttributesBuilder callFlowId(String callFlowId) {
            this.callFlowId = callFlowId;
            return this;
        }

        public CallingServiceApiAttributesBuilder callBackEndpoint(String callBackEndpoint) {
            this.callBackEndpoint = callBackEndpoint;
            return this;
        }

        public CallingServiceApiAttributes build() {
            return new CallingServiceApiAttributes(this);
        }
    }
}

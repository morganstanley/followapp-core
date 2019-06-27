package com.followapp.core.imimobile;

public class ImiMobileApiException extends RuntimeException {

    private static final long serialVersionUID = 3008779332689262679L;

    public ImiMobileApiException(String message) {
        super(message);
    }

    public ImiMobileApiException(String message, Throwable exception) {
        super(message, exception);
    }
}

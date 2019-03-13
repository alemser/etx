package com.alx.etx.client;

public class CoordinationClientException extends RuntimeException {

    public CoordinationClientException(String message) {
        super(message);
    }

    public CoordinationClientException(String message, Throwable cause) {
        super(message, cause);
    }
}

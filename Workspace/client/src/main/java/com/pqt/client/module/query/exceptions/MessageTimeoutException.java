package com.pqt.client.module.query.exceptions;

public class MessageTimeoutException extends Exception {
    public MessageTimeoutException() {
        super();
    }

    public MessageTimeoutException(String message) {
        super(message);
    }

    public MessageTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageTimeoutException(Throwable cause) {
        super(cause);
    }
}

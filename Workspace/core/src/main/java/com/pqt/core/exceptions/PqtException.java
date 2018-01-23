package com.pqt.core.exceptions;

public class PqtException extends Exception {

    public PqtException() {
        super();
    }

    public PqtException(String message) {
        super(message);
    }

    public PqtException(String message, Throwable cause) {
        super(message, cause);
    }

    public PqtException(Throwable cause) {
        super(cause);
    }

    public PqtException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

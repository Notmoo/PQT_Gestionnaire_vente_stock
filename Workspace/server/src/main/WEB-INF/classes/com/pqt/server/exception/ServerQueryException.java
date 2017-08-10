package com.pqt.server.exception;

public class ServerQueryException extends Exception {

    public ServerQueryException() {
    }

    public ServerQueryException(String message) {
        super(message);
    }

    public ServerQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerQueryException(Throwable cause) {
        super(cause);
    }
}

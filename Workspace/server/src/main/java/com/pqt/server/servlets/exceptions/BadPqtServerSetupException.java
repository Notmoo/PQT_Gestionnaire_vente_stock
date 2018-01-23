package com.pqt.server.servlets.exceptions;

import com.pqt.core.exceptions.PqtException;

public class BadPqtServerSetupException extends PqtException {
    public BadPqtServerSetupException() {
        super();
    }

    public BadPqtServerSetupException(String message) {
        super(message);
    }

    public BadPqtServerSetupException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadPqtServerSetupException(Throwable cause) {
        super(cause);
    }

    public BadPqtServerSetupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

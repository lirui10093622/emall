package org.emall.common.exception;

import java.io.Serializable;

public class EmallException extends Exception implements Serializable {
    public EmallException() {
        super();
    }

    public EmallException(String message) {
        super(message);
    }

    public EmallException(String message, Throwable cause) {
        super(message, cause);
    }
}
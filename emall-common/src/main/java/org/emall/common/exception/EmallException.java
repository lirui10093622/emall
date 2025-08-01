package org.emall.common.exception;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class EmallException extends RuntimeException implements Serializable {

    private String code;

    public EmallException(String code, String message) {
        super(message);
        this.code = code;
    }

    public EmallException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
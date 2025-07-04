package org.emall.common.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class EmallResponse implements Serializable {
    private String code;
    private String message;

    public EmallResponse() {

    }

    public EmallResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
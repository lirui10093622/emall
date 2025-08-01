package org.emall.common.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmallResponse<T> implements Serializable {
    private boolean success;
    private String code;
    private String message;
    private T data;

    public static EmallResponse<Void> success(String code, String message) {
        EmallResponse<Void> response = new EmallResponse<>();
        response.setSuccess(true);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static <T> EmallResponse<T> success(T data, String code, String message) {
        EmallResponse<T> response = new EmallResponse<>();
        response.setSuccess(true);
        response.setData(data);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static EmallResponse<Void> fail(String code, String message) {
        EmallResponse<Void> response = new EmallResponse<>();
        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
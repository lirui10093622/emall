package org.emall.common.response;

import lombok.Data;
import org.emall.common.enums.ApiResult;

import java.io.Serializable;

@Data
public class EmallResponse<T> implements Serializable {
    private String code;
    private String message;
    private T data;

    private EmallResponse() {
    }

    public static EmallResponse<Void> success() {
        return EmallResponse.success(null);
    }

    public static <T> EmallResponse<T> success(T data) {
        EmallResponse<T> response = new EmallResponse<>();
        response.setCode(ApiResult.SUCCESS.getCode());
        response.setMessage(ApiResult.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    public static <T> EmallResponse<T> fail(ApiResult result) {
        EmallResponse<T> response = new EmallResponse<>();
        response.setCode(result.getCode());
        response.setMessage(result.getMessage());
        return response;
    }

    public static <T> EmallResponse<T> fail(String code, String message) {
        EmallResponse<T> response = new EmallResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public boolean isSuccess() {
        return ApiResult.SUCCESS.getCode().equals(this.code);
    }
}
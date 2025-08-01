package org.emall.facade.vo;

import lombok.Data;
import org.emall.common.enums.ApiResultCode;

import java.io.Serializable;

@Data
public class ApiResponse implements Serializable {
    private String code;
    private String message;
    private Object data;

    public static ApiResponse success(Object data) {
        ApiResponse response = new ApiResponse();
        response.setCode(ApiResultCode.SUCCESS.getCode());
        response.setMessage(ApiResultCode.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    public static ApiResponse fail(String code, String message) {
        ApiResponse response = new ApiResponse();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
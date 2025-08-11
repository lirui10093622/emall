package org.emall.common.enums;

public enum ApiResult {
    SUCCESS("0000", "success"),
    FAIL("0001", "fail"),
    INVALID_PARAMETER("0002", "parameter not valid"),
    NO_PERMISSION("0003", "no permission"),
    ;

    ApiResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
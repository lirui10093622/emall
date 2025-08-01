package org.emall.common.enums;

public enum ApiResultCode {
    SUCCESS("0000", "success"),
    FAIL("0001", "fail"),
    PARAMETER_NOT_VALID("0002", "parameter not valid");

    ApiResultCode(String code, String message) {
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
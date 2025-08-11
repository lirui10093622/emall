package org.emall.common.exception;

import org.emall.common.enums.ApiResult;

public class InvalidParameterException extends EmallException {

    public InvalidParameterException(String message) {
        super(ApiResult.INVALID_PARAMETER.getCode(), message);
    }
}
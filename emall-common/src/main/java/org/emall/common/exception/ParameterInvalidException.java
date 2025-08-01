package org.emall.common.exception;

import org.emall.common.enums.ApiResultCode;

public class ParameterInvalidException extends EmallException {

    public ParameterInvalidException(String message) {
        super(ApiResultCode.PARAMETER_NOT_VALID.getCode(), message);
    }
}
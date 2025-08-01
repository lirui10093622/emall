package org.emall.facade.handler;

import lombok.extern.slf4j.Slf4j;
import org.emall.common.enums.ApiResultCode;
import org.emall.common.exception.EmallException;
import org.emall.common.response.EmallResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String MESSAGE_PREFIX_ARGUMENT_NOT_VALID = "请求参数不合法:";

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public EmallResponse<Void> handleException(Exception e) {
        log.error("handleException", e);
        return EmallResponse.fail(ApiResultCode.FAIL.getCode(), e.getMessage());
    }

    @ResponseStatus(code = HttpStatus.OK)
    @ExceptionHandler({EmallException.class})
    public EmallResponse<Void> handleEmallException(EmallException e) {
        log.error("handleEmallException", e);
        return EmallResponse.fail(e.getCode(), e.getMessage());
    }

    @ResponseStatus(code = HttpStatus.OK)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public EmallResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        return EmallResponse.fail(ApiResultCode.PARAMETER_NOT_VALID.getCode(), parseMessage(e));
    }

    protected String parseMessage(MethodArgumentNotValidException e) {
        StringBuilder ss = new StringBuilder(MESSAGE_PREFIX_ARGUMENT_NOT_VALID);
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError) {
                String field = ((FieldError) error).getField();
                String message = error.getDefaultMessage();
                ss.append(" ").append(field).append(": ").append(message).append(";");
            }
        }
        return ss.toString();
    }
}
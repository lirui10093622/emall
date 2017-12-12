package org.lr.expriment.emall.user.service.dto;

import java.io.Serializable;

/**
 * @author 李锐
 * @email lirui10093622@163.com
 * @time 2017-10-12 20:12:43
 */
public abstract class BaseResponse implements Serializable {

  private static final long serialVersionUID = -7108720465477255837L;

  protected BaseResponse(boolean success, String code, String message) {
    this.success = success;
    this.code = code;
    this.message = message;
  }

  private boolean success;
  private String code;
  private String message;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "BaseResponse{" +
        "success=" + success +
        ", code='" + code + '\'' +
        ", message='" + message + '\'' +
        '}';
  }
}

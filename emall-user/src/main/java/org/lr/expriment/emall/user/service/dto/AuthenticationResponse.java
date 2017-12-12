package org.lr.expriment.emall.user.service.dto;

/**
 * @author 李锐
 * @email lirui10093622@163.com
 * @time 2017-10-12 20:12:12
 */
public class AuthenticationResponse extends BaseResponse {

  public AuthenticationResponse(boolean success, String code, String message) {
    super(success, code, message);
  }
}

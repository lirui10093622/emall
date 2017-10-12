package org.lr.expriment.emall.user.service.dto;

/**
 * @author 李锐
 * @email rui.li04@mljr.com
 * @time 2017-10-12 20:19:53
 */
public class AuthenticationRequest extends BaseRequest {

  private String userName;
  private String password;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "AuthenticationRequest{" +
        "userName='" + userName + '\'' +
        ", password='" + password + '\'' +
        "} " + super.toString();
  }
}

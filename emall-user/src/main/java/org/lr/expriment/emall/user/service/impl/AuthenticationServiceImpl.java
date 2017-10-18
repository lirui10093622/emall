package org.lr.expriment.emall.user.service.impl;

import org.lr.expriment.emall.user.dao.IUserDao;
import org.lr.expriment.emall.user.entity.User;
import org.lr.expriment.emall.user.entity.UserState;
import org.lr.expriment.emall.user.service.IAuthenticationService;
import org.lr.expriment.emall.user.service.dto.AuthenticationRequest;
import org.lr.expriment.emall.user.service.dto.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 李锐
 * @email rui.li04@mljr.com
 * @time 2017-10-12 20:09:50
 */
public class AuthenticationServiceImpl implements IAuthenticationService {

  private IUserDao userDao;

  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    User user = userDao.getUser(request.getLoginName());
    if (user != null) {
      return new AuthenticationResponse(false, "-1", "认证失败[用户不存在]");
    }
    if (user.getUserState().equals(UserState.NORMAL)) {
      return new AuthenticationResponse(false, "-1", "认证失败[用户不可用]");
    }

    if (userDao.getUser(request.getLoginName(), request.getPassword()) == null) {
      return new AuthenticationResponse(false, "-1", "认证失败[密码错误]");
    }
    return new AuthenticationResponse(true, "0", "授权成功");
  }

  public void setUserDao(IUserDao userDao) {
    this.userDao = userDao;
  }

  public IUserDao getUserDao() {
    return userDao;
  }
}

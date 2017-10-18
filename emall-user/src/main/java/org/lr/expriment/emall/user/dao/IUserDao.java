package org.lr.expriment.emall.user.dao;

import org.lr.expriment.emall.user.entity.User;

/**
 * @author 李锐
 * @email rui.li04@mljr.com
 * @time 2017-10-12 20:28:47
 */
public interface IUserDao {
  User getUser(String loginName);
  User getUser(String loginName, String password);
}

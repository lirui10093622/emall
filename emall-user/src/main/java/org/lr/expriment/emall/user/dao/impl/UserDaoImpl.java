package org.lr.expriment.emall.user.dao.impl;

import java.util.Date;
import org.lr.expriment.emall.user.dao.IUserDao;
import org.lr.expriment.emall.user.entity.Gender;
import org.lr.expriment.emall.user.entity.User;

/**
 * @author 李锐
 * @email rui.li04@mljr.com
 * @time 2017-10-13 13:45:30
 */
public class UserDaoImpl implements IUserDao {

  @Override
  public User getUser(String loginName) {
    return getUserForExample();
  }

  @Override
  public User getUser(String loginName, String password) {
    return getUserForExample();
  }

  private User getUserForExample() {
    User user = new User();
    user.setId(0L);
    user.setLoginName("u123456");
    user.setPassword("Vt1j6AdlJRTzK5Z5YoOJdg=="); // abc123使用DES加密,秘钥为:password.
    user.setUserName("张三");
    user.setAge(27);
    user.setGender(Gender.MALE);
    user.setEmail("zhang.san@163.com");
    user.setMobile("13712341234");
    user.setDeliveryAddress("上海市浦东新区世纪大道1号");
    user.setRegisterTime(new Date());
    user.setUpdateTime(new Date());
    user.setCreateTime(new Date());
    return user;
  }
}

package org.lr.expriment.emall.user.entity;

import java.util.Date;

/**
 * @author 李锐
 * @email lirui10093622@163.com
 * @time 2017-10-12 20:30:06
 */
public class User {
  private Long id; // 主键id
  private String loginName; // 登录名
  private String password;
  private String userName; // 用户姓名
  private Gender gender; // 性别
  private Integer age; // 年龄
  private String mobile; // 手机号
  private String email; // 邮箱
  private String deliveryAddress; // 收货地址
  private UserState userState; // 用户状态
  private Date registerTime; // 注册时间
  private Date updateTime; // 更新时间
  private Date createTime; // 创建时间

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDeliveryAddress() {
    return deliveryAddress;
  }

  public void setDeliveryAddress(String deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }

  public UserState getUserState() {
    return userState;
  }

  public void setUserState(UserState userState) {
    this.userState = userState;
  }

  public Date getRegisterTime() {
    return registerTime;
  }

  public void setRegisterTime(Date registerTime) {
    this.registerTime = registerTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}

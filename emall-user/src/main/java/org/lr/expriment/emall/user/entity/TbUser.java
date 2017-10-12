package org.lr.expriment.emall.user.entity;

import java.util.Date;

/**
 * @author 李锐
 * @email rui.li04@mljr.com
 * @time 2017-10-12 20:30:06
 */
public class TbUser {
  private Long id; // 主键id
  private String loginName; // 登录名
  private String password;
  private String userName; // 用户姓名
  private Gender gender; // 性别
  private Integer age; // 年龄
  private String mobile; // 手机号
  private String email; // 邮箱
  private String deliveryAddress; // 收货地址
  private boolean active; // 用户状态
  private Date registerTime; // 注册时间
  private Date updateTime; // 更新时间
  private Date createTime; // 创建时间


}

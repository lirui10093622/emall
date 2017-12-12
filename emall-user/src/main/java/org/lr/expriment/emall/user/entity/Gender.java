package org.lr.expriment.emall.user.entity;

/**
 * 性别枚举
 *
 * @author 李锐
 * @email lirui10093622@163.com
 * @time 2017-10-12 20:31:10
 */
public enum Gender {

  MALE("M", "男"), FEMALE("F", "女");

  private String key;
  private String desc;

  private Gender(String key, String desc) {
    this.key = key;
    this.desc = desc;
  }
}

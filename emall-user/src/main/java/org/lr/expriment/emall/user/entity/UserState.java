package org.lr.expriment.emall.user.entity;

/**
 * @author 李锐
 * @email lirui10093622@163.com
 * @time 2017-10-17 14:02:23
 */
public enum UserState {

  NORMAL(0, "正常"), DISABLE(1, "无效"), FORBIDDEN(2, "禁用");

  private int state;
  private String desc;

  private UserState(int state, String desc) {
    this.state = state;
    this.desc = desc;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
}

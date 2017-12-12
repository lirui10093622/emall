package org.lr.expriment.emall.trade.actioin;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 李锐
 * @email lirui10093622@163.com
 * @time 2017-10-23 13:52:25
 */
public class WelcomePageAction {

  @RequestMapping("/welcome.html")
  public String showHomePage() {
    return "welcome";
  }
}
package org.lr.expriment.emall.user;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author 李锐
 * @email lirui10093622@163.com
 * @time 2017-10-13 13:48:09
 */
public class EmallUserApp {

  public static void main(String[] args) throws Exception {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    context.start();
    Thread.sleep(10000L);
  }
}

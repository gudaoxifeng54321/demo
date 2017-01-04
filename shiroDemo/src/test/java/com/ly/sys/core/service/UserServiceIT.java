package com.ly.sys.core.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ly.sys.core.user.service.UserService;

public class UserServiceIT {
  @Autowired
  private static UserService userService;
  
  @BeforeClass
  public static void before() throws Exception {
    ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:IOC.xml");
    userService = (UserService) ac.getBean("userService");
  }

  @Test
  public void add() {
    long start= System.currentTimeMillis();
    //User user = userService.findById(323410L);
    //int rt = userService.modifyUser(323407L, "ni646687909");

  }
}

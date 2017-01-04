package com.ly.sys.core.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ly.sys.core.user.service.PasswordService;

public class PasswordServiceTest {

  private PasswordService passwordService;

  @Before
  public void setUp() throws Exception {
    ApplicationContext context = new FileSystemXmlApplicationContext("classpath:IOC.xml");
    passwordService = context.getBean("passwordServiceImpl", PasswordService.class);
  }

  @Test
  public void test() {
    String pwd = passwordService.encryptPassword("viky", "123456");
    System.out.println(pwd);
  }
}

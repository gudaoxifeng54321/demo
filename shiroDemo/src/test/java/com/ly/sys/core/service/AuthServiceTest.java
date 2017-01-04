package com.ly.sys.core.service;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ly.common.json.Json;
import com.ly.sys.core.auth.service.AuthService;
import com.ly.sys.core.permission.vo.Role;
import com.ly.sys.core.user.vo.Operator;

public class AuthServiceTest {
  private AuthService               authService;
  private static ApplicationContext context;

  @Before
  public void setUp() throws Exception {
    context = new FileSystemXmlApplicationContext("classpath:IOC.xml");
    authService = context.getBean("authService", AuthService.class);
  }

  //@Test
  public void testFindRoles() {
    Operator operator = new Operator();
    operator.setId(Long.valueOf(6));
    Set<Role> roles = authService.findRoles(operator);
    System.out.println(Json.toJson(roles));
  }
  //@Test
  public void testFindStringRoles() {
    Operator operator = new Operator();
    operator.setId(Long.valueOf(6));
    Set<String> roles = authService.findStringRoles(operator);
    System.out.println(Json.toJson(roles));
  }
  @Test
  public void testFindStringPermissions() {
    Operator operator = new Operator();
    operator.setId(Long.valueOf(203));
    Set<String> roles = authService.findStringPermissions(operator);
    System.out.println(Json.toJson(roles));
  }
  
 /* public static void main(String args[]){
    for (int i = 0; i < 5; i++) {
      System.out.println(UUID.randomUUID().toString());
    }
  }*/
}

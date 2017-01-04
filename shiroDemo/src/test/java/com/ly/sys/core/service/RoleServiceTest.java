package com.ly.sys.core.service;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ly.common.json.Json;
import com.ly.sys.core.permission.service.RoleService;
import com.ly.sys.core.permission.vo.Role;

public class RoleServiceTest {
  private RoleService roleService;

  @Before
  public void setUp() throws Exception {
    ApplicationContext context = new FileSystemXmlApplicationContext("classpath:IoC.xml");
    roleService = context.getBean("roleServiceImpl", RoleService.class);
  }

  @Test
  public void testfindById() {
    Role role = roleService.findById(1L);
    System.out.println(Json.toJson(role));
  }

  @Test
  public void testfindRoleIds() {
  }

  /*
   * 现在echache没加缓存，暂时无法测试
   */
  @Test
  public void testfindShowRoles() {
    Set<Long> roles = new HashSet<>();
    roles.add(1L);
    roles.add(2L);
    Set<Role> role = roleService.findShowRoles(roles);
    System.out.println(Json.toJson(role));
  }

}

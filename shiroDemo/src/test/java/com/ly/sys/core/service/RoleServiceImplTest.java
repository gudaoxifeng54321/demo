package com.ly.sys.core.service;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ly.sys.core.permission.service.RoleService;
import com.ly.sys.core.permission.vo.Role;

public class RoleServiceImplTest {

  @Autowired
  private static RoleService roleService;
  
  @BeforeClass
  public static void before() throws Exception {
    ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:IOC.xml");
    roleService = (RoleService) ac.getBean("roleServiceImpl");
    
  }
  
  @Test
  public void findShowRoles() {
    Set<Long> ids = new HashSet<Long>();
    ids.add(1L);
    ids.add(2L);
    ids.add(3L);
    Set<Role> roles = roleService.findShowRoles(ids);
    for(Role role:roles){
      System.out.println(role.getName());
    }
  }

}

package com.ly.sys.core.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ly.sys.common.exception.SystemException;
import com.ly.sys.core.permission.service.Role2Resource2PermissionService;
import com.ly.sys.core.permission.vo.Role2Resource2Permission;

public class Role2Resource2PermissionServiceImplTest {

  @Autowired
  private static Role2Resource2PermissionService role2Resource2PermissionService;
  
  @BeforeClass
  public static void before() throws Exception {
    ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:IOC.xml");
    role2Resource2PermissionService = (Role2Resource2PermissionService) ac.getBean("role2Resource2PermissionService");
    
  }
  
  @Test
  public void auth() throws SystemException{
    Role2Resource2Permission  permission = new Role2Resource2Permission();
    permission.setRoleId(1L);
    permission.setResourceId(1L);
    permission.setPermissionIds("1");    
    List<String> list = new ArrayList<String>();
//    list.add("1");
//    list.add("2");
//    list.add("3");
//    list.add("4");
    role2Resource2PermissionService.updateAuth(permission,list);
  }

}

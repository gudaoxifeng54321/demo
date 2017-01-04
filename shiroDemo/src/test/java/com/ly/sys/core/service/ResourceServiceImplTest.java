package com.ly.sys.core.service;



import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ly.common.json.Json;
import com.ly.sys.core.resource.service.ResourceService;
import com.ly.sys.core.resource.vo.Resource;
import com.ly.sys.core.resource.vo.temp.Menu;
import com.ly.sys.core.user.vo.Operator;

public class ResourceServiceImplTest {

  @Autowired
  private static ResourceService resourceService;
  
  @BeforeClass
  public static void before() throws Exception {
    ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:IOC.xml");
    resourceService = (ResourceService) ac.getBean("resourceService");
    System.out.println("START THE RESOURCESERVICETEST");
  }
  
  @AfterClass
  public static void after() throws Exception{
    System.out.println("END THE RESOURCE_SERVICE_TEST");
  }
  
  /**
   * 获取操作员拥有权限的操作菜单
   */
  @Test
  public void findMenus() {
    Operator operator = new Operator();
    operator.setId(26L);
    List<Menu>  menus = resourceService.findMenus(operator);
    for (int i = 0; i < menus.size(); i++) {
      System.out.println(Json.toJsonExclodeExpose(menus.get(i)));
      
    }
  }
  
  /**
   * 添加新资源
   */
//  @Test
  public void add(){
    Resource resource = new Resource();
    
    resource.setName("测试name2");
    resource.setSource("测试够go22");
    resource.setIdentity("test2");
    resource.setParentId(18L);
    resource.setParentIds("0/1/");
    resource.setWeight(1);
    resource.setIsParent("0");
    resource.setIsShow(1);
    resource.setDeleted(0);
    
    resourceService.add(resource);
    
  }
  
  /**
   * 删除指定Id资源
   */
  @Test
  public void delete(){
    resourceService.delete(26L);
  }

}

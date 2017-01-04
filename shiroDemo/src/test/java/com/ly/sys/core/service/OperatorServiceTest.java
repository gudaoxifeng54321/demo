package com.ly.sys.core.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ly.sys.core.user.service.OperatorService;
import com.ly.sys.core.user.vo.Operator;

public class OperatorServiceTest {
  @Autowired
  private static OperatorService operatorService;
  
  @BeforeClass
  public static void before() throws Exception {
    ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:IOC.xml");
    operatorService = (OperatorService) ac.getBean("operatorService");
  }

  @Test
  public void testFindById() {
    long start= System.currentTimeMillis();
    Operator operator = operatorService.findById(203L);
    operatorService.update(operator);
    operatorService.findById(203L);
    
  }
}

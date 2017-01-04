package com.ly.sys.core.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ly.common.json.Json;
import com.ly.sys.core.user.service.OperatorStatusHistoryService;
import com.ly.sys.core.user.vo.Operator;
import com.ly.sys.core.user.vo.OperatorStatusHistory;

public class OperatorStatusHistoryServiceTest {
  private OperatorStatusHistoryService operatorStatusHistoryService;

  @Before
  public void setUp() throws Exception {
    ApplicationContext context = new FileSystemXmlApplicationContext("classpath:IOC.xml");
    operatorStatusHistoryService = context.getBean("operatorStatusHistoryServiceImpl", OperatorStatusHistoryService.class);
  }

  @Test
  public void testfindById() {
    OperatorStatusHistory operatorOnline = operatorStatusHistoryService.findById(1L);
    System.out.println(Json.toJson(operatorOnline));
  }
  @Test
  public void testgetLastReason() {
    Operator operator=new Operator();
    operator.setId(1L);
    String operatorOnline = operatorStatusHistoryService.getLastReason(operator);
    System.out.println(Json.toJson(operatorOnline));
  }
  @Test
  public void testfindLastHistory() {
    Operator operator=new Operator();
    operator.setId(1L);
    OperatorStatusHistory operatorOnline = operatorStatusHistoryService.findLastHistory(operator);
    System.out.println(Json.toJson(operatorOnline));
  }
}

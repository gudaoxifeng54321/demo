package com.ly.sys.core.service;

import java.util.Date;

import org.apache.shiro.session.mgt.OnlineSession;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ly.sys.core.user.dao.OperatorOnlineDao;
import com.ly.sys.core.user.vo.OperatorOnline;

public class OperatorOnlineServiceTest {
  @Autowired
  private static OperatorOnlineDao operatorOnlineService;
  
  @BeforeClass
  public static void before() throws Exception {
    ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:IOC.xml");
    operatorOnlineService = (OperatorOnlineDao) ac.getBean("operatorOnlineDao");
  }

  @Test
  public void testinsert() {
   OperatorOnline operatoronline=new OperatorOnline();
   operatoronline.setOperatorId(1L);
   operatoronline.setUsername("admin");
   operatoronline.setHost("127.0.0.1");
   operatoronline.setSystemHost("1111");
   operatoronline.setUserAgent("pc");
   operatoronline.setStatus("1");
   operatoronline.setStartTimestamp(new Date());
   operatoronline.setLastAccessTime(new Date());
   operatoronline.setTimeout(30L);
   OnlineSession onlineSession=new OnlineSession();
   onlineSession.setAttribute("aa", "11");
   onlineSession.setHost("168.0.0.1");
   operatoronline.setSession(onlineSession);
   operatorOnlineService.insert(operatoronline);
   System.out.println(operatoronline.getId());
  }
}

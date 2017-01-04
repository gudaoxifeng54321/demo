package com.ly.sys.common.web.validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.common.http.HttpAgent;

/**
 * <p>Description: 
 * <p>User: mtwu
 * <p>Date: 2016-1-5
 * <p>Version: 1.0
 */
public class SendMobileCode implements Runnable{
  
  private String url;
  private final static Logger log = LoggerFactory.getLogger(SendMobileCode.class);
  public SendMobileCode(String url){
    this.url = url;
  }
  @Override
  public void run() {
    String ret = HttpAgent.get(url);
    if (!"success".equals(ret.trim())) {
      log.error("发送验证码失败["+url+"]");
    }
  }
  
}

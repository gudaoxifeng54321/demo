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
public class SendUserBehavior implements Runnable{
  
  private String url;
  private String content;
  private final static Logger log = LoggerFactory.getLogger(SendUserBehavior.class);
  public SendUserBehavior(String url,String content){
    this.url = url;
    this.content = content;
  }
  @Override
  public void run() {
    String ret = HttpAgent.post(url, content);
    if (!"success".equals(ret.trim())) {
      log.error("发送用户行为失败["+url+"]");
    }
  }
  
}

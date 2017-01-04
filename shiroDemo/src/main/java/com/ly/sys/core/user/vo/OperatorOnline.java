package com.ly.sys.core.user.vo;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.shiro.session.mgt.OnlineSession;

import com.ly.sys.common.entity.AbstractEntity;

/**
 * <p>Description: this is OperatorOnline bean
 * <p>User: mtwu
 * <p>Date: 2016-6-15 15:41:11
 * <p>Version: 1.0
 */

public class OperatorOnline extends AbstractEntity<String> {
  private String id;
  //
  private Long   operatorId;
  //
  private String username;
  // 用户主机地址
  private String host;
  //
  private String systemHost;
  // 用户浏览器类型
  private String userAgent;
  //
  private String status;
  //
  private Date   startTimestamp;
  //
  private Date   lastAccessTime;
  //
  private Long   timeout;
  //
  
  private OnlineSession session;

  public Long getOperatorId() {
    return this.operatorId;
  }
  @Override
  public String getId() {
    return id;
  }
  @Override
  public void setId(String id) {
    this.id = id;
  }

  public void setOperatorId(Long operatorId) {
    this.operatorId = operatorId;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getHost() {
    return this.host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getSystemHost() {
    return this.systemHost;
  }

  public void setSystemHost(String systemHost) {
    this.systemHost = systemHost;
  }

  public String getUserAgent() {
    return this.userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getStartTimestamp() {
    return this.startTimestamp;
  }

  public void setStartTimestamp(Date startTimestamp) {
    this.startTimestamp = startTimestamp;
  }

  public Date getLastAccessTime() {
    return this.lastAccessTime;
  }

  public void setLastAccessTime(Date lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
  }

  public Long getTimeout() {
    return this.timeout;
  }

  public void setTimeout(Long timeout) {
    this.timeout = timeout;
  }

  public OnlineSession getSession() {
    return this.session;
  }

  public void setSession(OnlineSession session) {
    this.session = session;
  }
  public static final OperatorOnline fromOnlineSession(OnlineSession session) {
    OperatorOnline online = new OperatorOnline();
    online.setId(String.valueOf(session.getId()));
    online.setOperatorId(session.getUserId());
    online.setUsername(session.getUsername());
    online.setStartTimestamp(session.getStartTimestamp());
    online.setLastAccessTime(session.getLastAccessTime());
    online.setTimeout(session.getTimeout());
    online.setHost(session.getHost());
    online.setUserAgent(session.getUserAgent());
    online.setSystemHost(session.getSystemHost());
    online.setSession(session);
    online.setStatus(session.getStatus().name());

    return online;
}

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}

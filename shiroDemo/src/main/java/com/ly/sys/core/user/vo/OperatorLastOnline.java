package com.ly.sys.core.user.vo;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.ly.sys.common.entity.BaseEntity;

/**
 * <p>Description: this is OperatorLastOnline bean
 * <p>User: mtwu
 * <p>Date: 2016-6-15 15:07:01
 * <p>Version: 1.0
 */

public class OperatorLastOnline extends BaseEntity {

  /**
   * 
   */
  private static final long serialVersionUID = -1362174424801493572L;
  //
  private Long    operatorId;
  //
  private String  username;

  /**
   * 最后退出时的uid
   */
  private String  uid;

  // 用户主机地址
  private String  host;
  // 用户浏览器类型
  private String  userAgent;
  //
  private String  systemHost;
  //
  private Date    lastLoginTimestamp;
  //
  private Date    lastStopTimestamp;
  //
  private Integer loginCount = 0;
  //
  private Long    totalOnlineTime = 0L;

  public Long getOperatorId() {
    return this.operatorId;
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

  public String getUserAgent() {
    return this.userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public String getSystemHost() {
    return this.systemHost;
  }

  public void setSystemHost(String systemHost) {
    this.systemHost = systemHost;
  }

  public Date getLastLoginTimestamp() {
    return this.lastLoginTimestamp;
  }

  public void setLastLoginTimestamp(Date lastLoginTimestamp) {
    this.lastLoginTimestamp = lastLoginTimestamp;
  }

  public Date getLastStopTimestamp() {
    return this.lastStopTimestamp;
  }

  public void setLastStopTimestamp(Date lastStopTimestamp) {
    this.lastStopTimestamp = lastStopTimestamp;
  }

  public Integer getLoginCount() {
    return this.loginCount;
  }

  public void setLoginCount(Integer loginCount) {
    this.loginCount = loginCount;
  }

  public Long getTotalOnlineTime() {
    return this.totalOnlineTime;
  }

  public void setTotalOnlineTime(Long totalOnlineTime) {
    this.totalOnlineTime = totalOnlineTime;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public void incLoginCount() {
    setLoginCount(getLoginCount() + 1);
  }

  public void incTotalOnlineTime() {
    long onlineTime = getLastStopTimestamp().getTime() - getLastLoginTimestamp().getTime();
    setTotalOnlineTime((null == getTotalOnlineTime() ? 0 : getTotalOnlineTime()) + onlineTime / 1000);
  }

  public static final OperatorLastOnline fromOperatorOnline(OperatorOnline online) {
    OperatorLastOnline lastOnline = new OperatorLastOnline();
    lastOnline.setHost(online.getHost());
    lastOnline.setOperatorId(online.getOperatorId());
    lastOnline.setUsername(online.getUsername());
    lastOnline.setUserAgent(online.getUserAgent());
    lastOnline.setSystemHost(online.getSystemHost());
    lastOnline.setUid(String.valueOf(online.getId()));
    lastOnline.setLastLoginTimestamp(online.getStartTimestamp());
    lastOnline.setLastStopTimestamp(online.getLastAccessTime());
    return lastOnline;
  }

  public static final void merge(OperatorLastOnline from, OperatorLastOnline to) {
    to.setHost(from.getHost());
    to.setOperatorId(from.getOperatorId());
    to.setUsername(from.getUsername());
    to.setUid(from.getUid());
    to.setUserAgent(from.getUserAgent());
    to.setSystemHost(from.getSystemHost());
    to.setLastLoginTimestamp(from.getLastLoginTimestamp());
    to.setLastStopTimestamp(from.getLastStopTimestamp());
  }
}

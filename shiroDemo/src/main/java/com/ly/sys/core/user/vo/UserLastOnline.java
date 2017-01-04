package com.ly.sys.core.user.vo;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.ly.sys.common.entity.BaseEntity;

/**
 * <p>Description: this is UserLastOnline bean
 * <p>User: mtwu
 * <p>Date: 2016-6-12 16:33:31
 * <p>Version: 1.0
 */

public class UserLastOnline extends BaseEntity {

  //
  private Long    userId;
  //
  private String  username;
  //
  private Date    lastLoginTimestamp;
  //
  private Integer loginCount;
  // 用户主机地址
  private String  host;
  // 用户浏览器类型
  private String  userAgent;

  public Long getUserId() {
    return this.userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Date getLastLoginTimestamp() {
    return this.lastLoginTimestamp;
  }

  public void setLastLoginTimestamp(Date lastLoginTimestamp) {
    this.lastLoginTimestamp = lastLoginTimestamp;
  }

  public Integer getLoginCount() {
    return this.loginCount;
  }

  public void setLoginCount(Integer loginCount) {
    this.loginCount = loginCount;
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

  public void incLoginCount() {
    setLoginCount(getLoginCount() + 1);
  }



  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}

package com.ly.sys.core.user.vo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.ly.sys.common.entity.BaseEntity;

/**
 * <p>Description: this is User bean
 * <p>User: mtwu
 * <p>Date: 2016-6-12 16:33:30
 * <p>Version: 1.0
 */

public class User extends BaseEntity implements Serializable {

  // 平台来源标识
  @Expose
  private Long thirdId;
  @Expose
  private String  source;
  // 用户名称(登录名)
  @Expose
  private String  username;
  // 邮箱
  @Expose
  private String  email;
  //
  @Expose
  private String  mobile;
  // 用户密码
  private String  password;
  // model("模特"),photographer("摄影商”),seller("1B商家"),buyer("2b商家"),agent("代发商”),decorator("产品编辑")
  @Expose
  private Integer  type;
  //
  @Expose
  private Integer  deleted;
  //
  @Expose
  private Date    createDate;
  //
  @Expose
  private Integer status;

  @Expose
  private String  host;

  @Expose
  private String  timeStmap;        // 用户页面登录时候的时间戳

  private int     isEmailVerified;

  private int     isMobileVerified;
  
  private Integer newEncrypted;
  
  public User() {
    
  }

  public User(Long id,Long thirdId, String source, String username, String email, String mobile, String password, Integer type, Integer deleted,
      Date createDate, Integer status, int isEmailVerified, int isMobileVerified,Integer newEncrypted) {
    this.id = id;
    this.thirdId = thirdId;
    this.source = source;
    this.username = username;
    this.email = email;
    this.mobile = mobile;
    this.password = password;
    this.type = type;
    this.deleted = deleted;
    this.createDate = createDate;
    this.status = status;
    this.isEmailVerified = isEmailVerified;
    this.isMobileVerified = isMobileVerified;
    this.newEncrypted = newEncrypted;
  }

  
  public Integer getNewEncrypted() {
    return newEncrypted;
  }

  public void setNewEncrypted(Integer newEncrypted) {
    this.newEncrypted = newEncrypted;
  }
  
  public Long getThirdId() {
    return thirdId;
  }

  public void setThirdId(Long thirdId) {
    this.thirdId = thirdId;
  }

  public String getSource() {
    return this.source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobile() {
    return this.mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Integer getType() {
    return this.type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getDeleted() {
    return this.deleted;
  }

  public void setDeleted(Integer deleted) {
    this.deleted = deleted;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Integer getStatus() {
    return this.status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getTimeStmap() {
    return timeStmap;
  }

  public void setTimeStmap(String timeStmap) {
    this.timeStmap = timeStmap;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public int getIsEmailVerified() {
    return isEmailVerified;
  }

  public void setIsEmailVerified(int isEmailVerified) {
    this.isEmailVerified = isEmailVerified;
  }

  public int getIsMobileVerified() {
    return isMobileVerified;
  }

  public void setIsMobileVerified(int isMobileVerified) {
    this.isMobileVerified = isMobileVerified;
  }

}

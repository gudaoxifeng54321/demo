package com.ly.sys.core.user.vo;

import com.google.gson.annotations.Expose;
import com.ly.sys.common.entity.BaseEntity;

public abstract class BaseUser extends BaseEntity{
  @Expose
  protected String username;
  protected String password;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}

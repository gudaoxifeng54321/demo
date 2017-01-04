package com.ly.sys.common.enums;
/**
 * <p>Description: 
 * <p>User: mtwu
 * <p>Date: 2015-12-9
 * <p>Version: 1.0
 */
public enum UserStatus {

  normal("正常状态",1), blocked("封禁状态",0);

  private final String info;
  private final int value;

  private UserStatus(String info,int value) {
      this.info = info;
      this.value=value;
  }

  public String getInfo() {
      return info;
  }

  public int getValue() {
    return value;
  }
  
}

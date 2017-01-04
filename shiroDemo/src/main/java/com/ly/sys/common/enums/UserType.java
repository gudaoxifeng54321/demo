package com.ly.sys.common.enums;
/**
 * <p>Description: 
 * <p>User: mtwu
 * <p>Date: 2015-12-9
 * <p>Version: 1.0
 */
public enum UserType {

  model("模特"),photographer("摄影商"),vendors("供应商"),admin("管理员");

  private final String info;

  private UserType(String info) {
      this.info = info;
  }

  public String getInfo() {
      return info;
  }
}

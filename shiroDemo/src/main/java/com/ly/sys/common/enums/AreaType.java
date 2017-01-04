package com.ly.sys.common.enums;
/**
 * <p>Description: 
 * <p>User: mtwu
 * <p>Date: 2015-12-18
 * <p>Version: 1.0
 */
public enum AreaType {

  county("国家"),area("区域"),province("省份"),city("城市"),region("区县"),street("街道");
  
  private final String info;

  private AreaType(String info) {
      this.info = info;
  }

  public String getInfo() {
      return info;
  }
}

package com.ly.common.cloud;
/**
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source code file is protected by copyright law and international
 * treaties. Unauthorized distribution of source code files, programs, or portion of the package, may result in severe civil and criminal
 * penalties, and will be prosecuted to the maximum extent under the law.
 * 
 * 七牛上传文件响应信息类
 * @author gaofeng
 * @since 2015-12-21
 */
public class SevenBullResponseInfo {
  private long fsize;
  private String key;
  private String hash;
  private int width;
  private int height;
  public long getFsize() {
    return fsize;
  }
  public void setFsize(long fsize) {
    this.fsize = fsize;
  }
  public String getKey() {
    return key;
  }
  public void setKey(String key) {
    this.key = key;
  }
  public String getHash() {
    return hash;
  }
  public void setHash(String hash) {
    this.hash = hash;
  }
  public int getWidth() {
    return width;
  }
  public void setWidth(int width) {
    this.width = width;
  }
  public int getHeight() {
    return height;
  }
  public void setHeight(int height) {
    this.height = height;
  }
}

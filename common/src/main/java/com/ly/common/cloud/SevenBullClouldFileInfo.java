package com.ly.common.cloud;

public class SevenBullClouldFileInfo {
  public String key;
  public long   size;
  public String mimeType;
  public long   putTime;

  public SevenBullClouldFileInfo(String key, long size, String mimeType, long putTime) {
    this.key = key;
    this.size = size;
    this.mimeType = mimeType;
    this.putTime = putTime;
  }

  @Override
  public boolean equals(Object obj) {
    return this.key.equals(((SevenBullClouldFileInfo) obj).key);
  }
}

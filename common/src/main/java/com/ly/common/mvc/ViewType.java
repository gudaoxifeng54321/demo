package com.ly.common.mvc;

public enum ViewType {
  jsp("jsp"), flt("flt"), json("json"), html("html");

  private final String type;

  private ViewType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}

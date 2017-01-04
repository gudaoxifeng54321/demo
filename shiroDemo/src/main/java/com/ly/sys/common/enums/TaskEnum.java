package com.ly.sys.common.enums;

public enum TaskEnum {
  SYNC_USER("sysc_user_task"), SYNC_OPERATOR("sysc_operator_task");
  String info;

  private TaskEnum(String info) {
    this.info = info;
  }

  public String getInfo() {
    return info;
  }
}

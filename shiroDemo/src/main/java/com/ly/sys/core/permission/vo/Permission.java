package com.ly.sys.core.permission.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.ly.sys.common.entity.BaseEntity;

/**
 * <p>
 * Description: this is Permission bean
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-12 16:33:31
 * <p>
 * Version: 1.0
 */

public class Permission extends BaseEntity {

  // 权限名称
  @Expose
  private String  name;
  // 权限标识
  @Expose
  private String  permission;
  // 权限描述
  @Expose
  private String  description;
  // 是否显示 0否 1是
  @Expose
  private Integer isShow;
  // 是否删除 0否 1是
  @Expose
  private Integer deleted = 0;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPermission() {
    return this.permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public Integer getIsShow() {
    return isShow;
  }

  public void setIsShow(Integer isShow) {
    this.isShow = isShow;
  }

  public Integer getDeleted() {
    return deleted;
  }

  public void setDeleted(Integer deleted) {
    this.deleted = deleted;
  }
}

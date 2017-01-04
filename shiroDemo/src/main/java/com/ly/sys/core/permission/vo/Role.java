package com.ly.sys.core.permission.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.ly.sys.common.entity.BaseEntity;
import com.ly.sys.core.resource.vo.Resource;

/**
 * <p>
 * Description: this is Role bean
 * <p>
 * User: mtwu
 * <p>O
 * Date: 2016-6-12 16:33:32
 * <p>
 * Version: 1.0
 */

public class Role extends BaseEntity {

  // 权限名称
  @Expose
  private String                         name;
  // 权限标识
  @Expose
  private String                         role;
  // 权限描述
  @Expose
  private String                         description;
  // 是否显示 0否 1是
  @Expose
  private Integer                         isShow;
  // 是否删除 0否 1是
  @Expose
  private Integer                         deleted=0;
  private List<Role2Resource2Permission> resource2Permissions;
  
  //多对多关系
  @Expose
  private List<Resource> resources;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRole() {
    return this.role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getIsShow() {
    return this.isShow;
  }

  public void setIsShow(Integer newStatus) {
    this.isShow = newStatus;
  }

  public Integer getDeleted() {
    return this.deleted;
  }

  public void setDeleted(Integer deleted) {
    this.deleted = deleted;
  }
  
  public List<Resource> getResources() {
    return resources;
  }

  public void setResources(List<Resource> resources) {
    this.resources = resources;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public List<Role2Resource2Permission> getResource2Permissions() {
    return resource2Permissions;
  }

  public void setResource2Permissions(List<Role2Resource2Permission> resource2Permissions) {
    this.resource2Permissions = resource2Permissions;
  }
  
  public void addResourcePermission(Role2Resource2Permission role2Resource2Permission){
    if(null == this.resource2Permissions){
      this.resource2Permissions = new ArrayList<Role2Resource2Permission>();
    }
    this.resource2Permissions.add(role2Resource2Permission);
  }
}

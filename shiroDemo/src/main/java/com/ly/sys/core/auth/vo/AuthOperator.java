package com.ly.sys.core.auth.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.ly.sys.common.entity.BaseEntity;

/**
 * <p>
 * Description: this is AuthOperator bean
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-12 16:33:32
 * <p>
 * Version: 1.0
 */

public class AuthOperator extends BaseEntity {

  // 权限名称
  private Long   operatorId;
  // 权限标识 逗号分隔
  private String roleIds;

  public Long getOperatorId() {
    return this.operatorId;
  }

  public void setOperatorId(Long operatorId) {
    this.operatorId = operatorId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public String getRoleIds() {
    return roleIds;
  }

  public void setRoleIds(String roleIds) {
    this.roleIds = roleIds;
  }
}

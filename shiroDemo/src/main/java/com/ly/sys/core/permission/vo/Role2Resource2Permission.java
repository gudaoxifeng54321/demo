package com.ly.sys.core.permission.vo;


import org.apache.commons.lang.builder.ToStringBuilder;

import com.ly.sys.common.entity.BaseEntity;


 /**
 * <p>Description: this is Role2Resource2Permission bean
 * <p>User: mtwu
 * <p>Date: 2016-6-12 16:33:32
 * <p>Version: 1.0
 */

public class Role2Resource2Permission  extends BaseEntity {

					//权限名称
    private Long roleId;
    			//权限标识
    private Long resourceId;
    			//权限描述
    private String permissionIds;
    public  Role2Resource2Permission(){
      
    }
		public  Role2Resource2Permission(Long roleId,Long resourceId,String permissionIds){
		  this.roleId = roleId;
		  this.resourceId = resourceId;
		  this.permissionIds = permissionIds;
		}
    
    
	public Long getRoleId() {
		return this.roleId;
	}
	
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
				
	public Long getResourceId() {
		return this.resourceId;
	}
	
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	
				
	public String getPermissionIds() {
		return this.permissionIds;
	}
	
	public void setPermissionIds(String permissionIds) {
		this.permissionIds = permissionIds;
	}
	
			@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

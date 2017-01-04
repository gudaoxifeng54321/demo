package com.ly.sys.core.permission.vo;


import org.apache.commons.lang.builder.ToStringBuilder;

import com.ly.sys.common.entity.BaseEntity;


 /**
 * <p>Description: this is Resource2Permission bean
 * <p>User: mtwu
 * <p>Date: 2016-6-12 16:33:32
 * <p>Version: 1.0
 */

public class Resource2Permission  extends BaseEntity {

					//权限名称
    private Long resourceId;
    			//权限编码
    private Long permissionId;
    	
					
	public Long getResourceId() {
		return this.resourceId;
	}
	
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	
				
	public Long getPermissionId() {
		return this.permissionId;
	}
	
	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}
	
			@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

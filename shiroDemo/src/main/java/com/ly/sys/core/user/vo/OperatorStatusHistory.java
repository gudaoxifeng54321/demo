package com.ly.sys.core.user.vo;


import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.ly.sys.common.entity.BaseEntity;


 /**
 * <p>Description: this is OperatorStatusHistory bean
 * <p>User: mtwu
 * <p>Date: 2016-6-15 15:07:01
 * <p>Version: 1.0
 */

public class OperatorStatusHistory  extends BaseEntity {

					//
    private Long operatorId;
    			//
    private Integer status;
    			//用户主机地址
    private String reason;
    			//用户浏览器类型
    private Long opOperatorId;
    			//
    private Date opDate;
    //操作员
    private Operator operator;
    	
					
	public Operator getOperator() {
      return operator;
    }

    public void setOperator(Operator operator) {
      this.operator = operator;
    }

  public Long getOperatorId() {
		return this.operatorId;
	}
	
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	
				
	public Integer getStatus() {
		return this.status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
				
	public String getReason() {
		return this.reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
				
	public Long getOpOperatorId() {
		return this.opOperatorId;
	}
	
	public void setOpOperatorId(Long opOperatorId) {
		this.opOperatorId = opOperatorId;
	}
	
				
	public Date getOpDate() {
		return this.opDate;
	}
	
	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}
	
			@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

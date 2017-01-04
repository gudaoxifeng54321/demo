package com.ly.sys.core.user.vo;

import java.util.Date;

import com.ly.sys.common.entity.BaseEntity;

public class EditLog extends BaseEntity{
  private Long id;
  private Long userId;
  private String content;
  private int type; //1:修改用户名 2:修改密码
  private Date createDate;
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public Long getUserId() {
    return userId;
  }
  public void setUserId(Long userId) {
    this.userId = userId;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public int getType() {
    return type;
  }
  public void setType(int type) {
    this.type = type;
  }
  public Date getCreateDate() {
    return createDate;
  }
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
  
  
  
}

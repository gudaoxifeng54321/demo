package com.ly.common.json;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;

public class Result<T> {
  @Expose
  private Integer code = -1; //返回状态码
  @Expose
  private T data; //是否存在数据信息
  @Expose
  private String msg;

  
  public Result(){
    
  }
  
  
  
  public Result(Integer code, T data, String msg) {
    super();
    this.code = code;
    this.data = data;
    this.msg = msg;
  }
  
  public void setResult(T data, String msg,Integer code) {
    this.code = code;
    this.data = data;
    this.msg = msg;
  }
  
  public void setResult(Integer code,T data,String key,Object... args) {
    this.code = code;
    this.data = data;
    try {
      this.msg = MessageUtils.message(key, args);
    } catch (Exception e) {
      this.msg = "未知异常";
    }
  }
  
  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
 
}

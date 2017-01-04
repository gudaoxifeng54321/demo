package com.ly.common.mvc;

import java.io.Serializable;
import java.util.HashMap;

import javax.servlet.http.Cookie;

import com.ly.common.X;
import com.ly.common.json.Json;

/**
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source code file is protected by copyright law and international
 * treaties. Unauthorized distribution of source code files, programs, or portion of the package, may result in severe civil and criminal
 * penalties, and will be prosecuted to the maximum extent under the law.
 * 
 * 所有Controller 基类, 提供一些对box操作的公共方法
 * 
 * @author gaofeng
 * @since 2015-10-30
 */
@SuppressWarnings("serial")
public class Box implements Serializable {
  private HashMap<String, String> parameter;
  private HashMap<String, Object> attribute;
  private HashMap<String, Cookie> cookie;
  private PageView                pageView;
  private Pagination              pagination;

  public Box() {
    pageView = new PageView();
    pagination = new Pagination();
    parameter = new HashMap<String, String>(10);
    attribute = new HashMap<String, Object>(20);
    cookie = new HashMap<String, Cookie>(10);
  }

  /**
   * 将当前box 转化成 json
   */
  @Override
  public String toString() {
    return Json.toJson(this);
  }

  /**
   * 将对象放入attribute中
   * 
   * @param key
   * @param value
   */
  public void setAttribute(String key, Object value) {
    attribute.put(key, value);
  }

  /**
   * remove a cookie from client browser (by setting the maxage=0)
   * 
   * @param key
   * @return
   */
  public boolean removeCookie(String key) {
    Cookie c = cookie.get(key);
    if (null == c) {
      return false;
    }
    c = new Cookie(c.getName(), "");
    c.setMaxAge(0);
    cookie.put(c.getName(), c);
    return true;
  }

  /**
   * 将cookie 添加到box中
   * 
   * @param key
   * @param c
   */
  public void setCookie(String key, Cookie c) {
    cookie.put(key, c);
  }

  /**
   * 获取指定key的 attribute
   * 
   * @param key
   * @return
   */
  public Object $a(String key) {
    return attribute.get(key);
  }

  /**
   * 获取指定key的 parameter
   * 
   * @param key
   * @return
   */
  public String $p(String key) {
    return parameter.get(key);
  }

  /**
   * 获取指定key的cookie对象
   * 
   * @param key
   * @return
   */
  public Cookie $c(String key) {
    Cookie c = cookie.get(key);
    if (null != c) {

      return c;
    } else {
      // 未找到明文cookie 返回加密cookie
      return cookie.get(X.ENCRYPTED + key);
    }
  }

  /**
   * 获取指定key的 cookie值
   * 
   * @param key
   * @return
   */
  public String $cv(String key) {
    Cookie c = cookie.get(key);
    if (null != c) {
      // 存在明文cookie 直接返回
      return c.getValue();
    } else {
      c = cookie.get(X.ENCRYPTED + key);
      if (null != c) {
        return c.getValue();
      } else {
        return null;
      }
    }
  }

  // --------------------- Getter & Setter ------------------------

  public HashMap<String, String> getParameter() {
    return parameter;
  }

  public void setParameter(HashMap<String, String> parameter) {
    this.parameter = parameter;
  }

  public HashMap<String, Object> getAttribute() {
    return attribute;
  }

  public void setAttribute(HashMap<String, Object> attribute) {
    this.attribute = attribute;
  }

  public HashMap<String, Cookie> getCookie() {
    return cookie;
  }

  public void setCookie(HashMap<String, Cookie> cookie) {
    this.cookie = cookie;
  }

  public PageView getPageView() {
    return pageView;
  }

  public void setPageView(PageView pageView) {
    this.pageView = pageView;
  }

  public Pagination getPagination() {
    return pagination;
  }

  public void setPagination(Pagination pagination) {
    this.pagination = pagination;
  }

}

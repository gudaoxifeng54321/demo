package com.ly.common.mvc;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ly.common.X;

/**
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source code file is protected by copyright law and international
 * treaties. Unauthorized distribution of source code files, programs, or portion of the package, may result in severe civil and criminal
 * penalties, and will be prosecuted to the maximum extent under the law.
 * 
 * 公共Interceptor, 所有Controller 都拦截.
 * 
 * @author gaofeng
 * @since 2015-10-30
 */
public class CommonInterceptor extends HandlerInterceptorAdapter {
  private final static Logger log            = LoggerFactory.getLogger(CommonInterceptor.class);
  private static final String CTX            = "ctx";
  private static final String InterceptorUrl = "/userCenter";
  private static final String LoginUrl       = "/login";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    request.setAttribute(CTX, request.getContextPath());
    PageView pv = new PageView();
    ClientInformationHandler.load(request, pv);
    // exclude login
    if (pv.getUrl().contains(LoginUrl) || pv.getUrl().contains("/logout")) {
      return true;
    }

    // 获取refer地址
    String refer = pv.getReferer();
    if (!StringUtils.isEmpty(refer)) {
      if (refer.indexOf("/register") != -1 || refer.indexOf("/login") != -1 || refer.indexOf("/logout") != -1) {

      } else {
        request.getSession().setAttribute("refer", refer);
      }
    }

    Box box = new Box();
    String userId = box.$cv(X.USER);
    // Object user = request.getSession().getAttribute(X.USER);

    Object uIdObj = request.getSession().getAttribute(X.USER);
    Object uTypeObj = request.getSession().getAttribute(X.USER_TYPE);
    if (userId == null || userId.isEmpty() || uIdObj != null) {
      userId = (String) uIdObj;
    }
    int userType = Integer.MIN_VALUE;
    try {
      if (uTypeObj != null)
        userType = Integer.parseInt((String) uTypeObj);// Integer.parseInt(box.$cv(X.USER_TYPE));
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (userId == null || userId.isEmpty()) {
      // TODO define interceptor in web.config maybe better
      // not logged in
      if (pv.getUrl().contains(InterceptorUrl)) {
        try {
          response.sendRedirect(LoginUrl);
        } catch (IOException e) {
          e.printStackTrace();
        }
        return false;
      } else {
        return true;
      }

    } else {
      // has logged in
      String url = pv.getUrl();
      if (url.contains(InterceptorUrl)) {
        switch (userType) {
        // seller
        case 0:
          if (!url.contains(InterceptorUrl + "/seller")) {
            response.sendRedirect(LoginUrl); // userCenter/seller
            return false;
          } else
            return true;

          // supplier
        case 1:
          if (!url.contains(InterceptorUrl + "/supplier")) {
            response.sendRedirect(LoginUrl); // userCenter/supplier
            return false;
          } else
            return true;
          // photography
        case 2:
          return true;
          // dropshipper
        case 3:
          return true;
        default:
          return false;
        }
      } else {
        return true;
      }
    }

    // return authentication(request, response);
  }

  /**
   * 本方法仅供开发阶段使用.
   * 
   * @param request
   * @param response
   * @return
   */
  private boolean authentication(HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cs = request.getCookies();
    if (null == cs) {
      // user的值为null
      try {
        response.sendRedirect("/signIn/index");
        // 跳转到登陆页面
      } catch (IOException e) {
        e.printStackTrace();
      }
      return false;
    }
    String userId = null;
    for (Cookie c : cs) {
      if (X.USER.equals(c.getName())) {
        // cookie中找到user 属性
        userId = c.getValue();
      }
    }
    if (null == userId) {
      // user的值为null
      try {
        response.sendRedirect("/signIn/index");
        return false;
        // 跳转到登陆页面
      } catch (IOException e) {
        e.printStackTrace();
      }
      // 还未登陆过
    } else {
      // 已经登录
      switch (userId) {
      case "51":// 1B用户的ID
        break;
      case "6937":// 替换成 2B用户的ID
        break;
      case "3":// 替换成 代发商用户的ID
        break;
      default:
        // user的值为null
        try {
          response.sendRedirect("/signIn/index");
          // 跳转到登陆页面
        } catch (IOException e) {
          e.printStackTrace();
        }
        return false;
      }
    }
    return true;
  }
}

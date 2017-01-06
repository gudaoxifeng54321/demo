package com.ly.common.mvc;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.ly.common.X;
import com.ly.common.encrypt.AES;

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
public class BaseController {
  private final static Logger log               = LoggerFactory.getLogger(BaseController.class);
  private final String        SESSION_TOKEN_TAG = "secToken";
  private final String        WEB_TOKEN_TAG     = "webToken";
  @Resource
  protected AES               aes;

  /**
   * 加载request中的 parameter,cookie,attribute,browserInfo 到box 中
   * 
   * @param request
   * @param bean
   */
  protected Box loadNewBox(HttpServletRequest request) {
    log.debug("BaseController.loadNewBox()");
    Box box = new Box();
    loadParameter(request, box);
    loadCookie(request, box);
    loadAttribute(request, box);
    ClientInformationHandler.load(request, box.getPageView());
    loadPagination(box);
    return box;
  }

  protected void loadPagination(Box box) {
    int pageNumber = X.string2int(box.$p(X.PAGE_NUMBER));
    int pageSize = X.string2int(box.$p(X.PAGE_SIZE));
    int pageNavigationSize = X.string2int(box.$p(X.PAGE_NAVIGATION_SIZE));
    if (pageNumber > 0) {
      box.getPagination().setPageNum(pageNumber);
    }
    if (pageSize > 0) {
      box.getPagination().setPageSize(pageSize);
    }
    if (pageNavigationSize > 0) {
      box.getPagination().setNavigationSize(pageNavigationSize);
    }
  }

  /**
   * 加载request中得所有 attribute 到box
   * 
   * @param request
   * @param bean
   */
  @SuppressWarnings("unchecked")
  protected void loadAttribute(HttpServletRequest request, Box box) {
    log.debug("BaseController.loadAttribute()");
    Enumeration<String> names = request.getAttributeNames();
    String n;
    while (names.hasMoreElements()) {
      n = names.nextElement();
      box.getAttribute().put(n, request.getAttribute(n));
    }
  }

  /**
   * 加载request 中所有parameter 到box 中
   * 
   * @param request
   * @param bean
   */
  @SuppressWarnings("unchecked")
  protected void loadParameter(HttpServletRequest request, Box box) {
    log.debug("BaseController.loadParameter()");
    Enumeration<String> names = request.getParameterNames();
    String n;
    while (names.hasMoreElements()) {
      n = names.nextElement();
      box.getParameter().put(n, $(request, n));
    }
  }

  /**
   * 将request中的cookie全部放入box中, 必要时进行aes解密
   * 
   * @param request
   * @param bean
   */
  protected void loadCookie(HttpServletRequest request, Box box) {
    log.debug("BaseController.loadCookie()");
    Cookie[] clientCookiesArray = request.getCookies();
    if (null == clientCookiesArray) {
      return;
    }
    for (Cookie c : clientCookiesArray) {
      if (c.getName().startsWith(X.ENCRYPTED)) {
        // name 以+打头的cookie 内容加密, 需要解密后放入box
        c.setValue(aes.decrypt(c.getValue()));
      }
      // 未含有+的cookie 直接放入 box
      box.getCookie().put(c.getName(), c);
    }
  }

  /**
   * 将box中的所有cookie 写到浏览器, X.ENCRYPTED 打头的cookie 进行aes加密
   * 
   * @param bean
   * @param response
   */
  protected void writeCookies(Box box, HttpServletResponse response) {
    log.debug("BaseController.writeCookies()");
    Set<String> keys = box.getCookie().keySet();
    for (String k : keys) {
      Cookie c = box.getCookie().get(k);
      if (c.getName().startsWith(X.ENCRYPTED)) {
        // 需要进行 aes加密
        c.setValue(aes.encrypt(c.getValue()));
      }
      c.setPath(X.WEB_ROOT);
      // c.setDomain("");//X.getConfig("DOMAIN")
      response.addCookie(c);
    }
  }

  /**
   * 获取request中指定key的 parameter
   * 
   * @param name
   * @return
   */
  protected String $(HttpServletRequest request, String key) {
    log.debug("BaseController.$()");
    return request.getParameter(key);
  }

  /**
   * 获取request 中指定的key的 parameter String 数组
   * 
   * @param name
   * @return
   */
  protected String[] $$(HttpServletRequest request, String name) {
    log.debug("BaseController.$$()");
    return request.getParameterValues(name);
  }

  /**
   * 创建 ModelAndView 并加载box 中的attribute.
   * 
   * @param viewName
   * @param bean
   * @return
   */
  protected ModelAndView createModelAndView(String viewName, Box box) {
    log.debug("BaseController.createModelAndView()");
    ModelAndView mv = new ModelAndView(viewName);
    mv.addAllObjects(box.getAttribute());
    return mv;
  }

  protected String getRandomNumber() {
    return RandomStringUtils.randomNumeric(18);
  }

  protected void setToken(HttpServletRequest request) {
    String theToken = getRandomNumber();
    WebUtils.setSessionAttribute(request, SESSION_TOKEN_TAG, theToken);
    request.setAttribute(WEB_TOKEN_TAG, theToken);
  }

  protected void resetToken(HttpServletRequest request) {
    WebUtils.setSessionAttribute(request, SESSION_TOKEN_TAG, getRandomNumber());
  }

  protected boolean validToken(HttpServletRequest request) {
    Object obj = WebUtils.getSessionAttribute(request, SESSION_TOKEN_TAG);
    String secToken = null;
    if (obj != null) {
      secToken = (String) obj;
    }
    String webToken = request.getParameter(WEB_TOKEN_TAG);
    if (secToken == null) {
      return true;
    } else if (webToken != null && secToken.equals(webToken)) {
      resetToken(request);
      return true;
    }
    return false;
  }

  /**
   * 创建 无model的view
   * 
   * @param viewName
   * @return
   */
  protected ModelAndView createModelAndView(String viewName) {
    log.debug("BaseController.createModelAndView()");
    ModelAndView mv = new ModelAndView(viewName);
    return mv;
  }

  /**
   * 不通过渲染 直接输出 文本/字符串/xml/JSON 等
   * 
   * @param response
   * @param content
   */
  protected void render(Object obj, HttpServletResponse response) {
    response.reset();
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setContentType("text/" + "html" + "; charset=utf-8");
    try {
      response.getWriter().write(obj.toString());
      response.getWriter().flush();
      response.getWriter().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // --------------------- Getter & Setter ------------------------

  public AES getAes() {
    return aes;
  }

  public void setAes(AES aes) {
    this.aes = aes;
  }
}

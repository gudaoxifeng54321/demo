package com.ly.sys.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ly.common.X;
import com.ly.common.http.IpUtils;
import com.ly.sys.common.web.validate.SendUserBehavior;

public class LogUtils {

    public static final Logger ERROR_LOG = LoggerFactory.getLogger("sys-error");
    public static final Logger ACCESS_LOG = LoggerFactory.getLogger("sys-access");
    public static final Logger SYNC_USER_LOG = LoggerFactory.getLogger("sync-user-log");
    public static final Logger SYNC_VDIAN_PUBLISH_LOG = LoggerFactory.getLogger("sync-vdian-publish-log");
    public static final Logger SYNC_PRODUCT_LOG = LoggerFactory.getLogger("sync-product-log");
    public static final Logger SYNC_OPERATOR_LOG = LoggerFactory.getLogger("sync-operator-log");
    public static final Logger SYNC_USERTOGSB_LOG = LoggerFactory.getLogger("sync-userToGsb-log");
    /**
     * 记录访问日志
     * [username][jsessionid][ip][accept][UserAgent][url][params][Referer]
     *
     * @param request
     */
    public static void logAccess(HttpServletRequest request) {
        String username = getUsername();
        String jsessionId = request.getRequestedSessionId();
        String ip = IpUtils.getIpAddr(request);
        String accept = request.getHeader("accept");
        String userAgent = request.getHeader("User-Agent");
        String url = request.getRequestURI();
        String params = getParams(request);
        String headers = getHeaders(request);

        StringBuilder s = new StringBuilder();
        s.append(getBlock(username));
        s.append(getBlock(jsessionId));
        s.append(getBlock(ip));
        s.append(getBlock(accept));
        s.append(getBlock(userAgent));
        s.append(getBlock(url));
        s.append(getBlock(params));
        s.append(getBlock(headers));
        s.append(getBlock(request.getHeader("Referer")));
        getAccessLog().info(s.toString());
        
        //用户行为
        String behaviorHost = X.getConfig("com.go2plus.globle.behavior.host");
        if(StringUtils.isEmpty(behaviorHost)) return;
        StringBuffer behaviorUrl = new StringBuffer("http://" + behaviorHost + "/addUserBehavior");
        StringBuffer behaviorContent = new StringBuffer();
        behaviorContent.append("ip=").append(ip);
        behaviorContent.append("&url=").append(url);
        behaviorContent.append("&host=").append(request.getServerName());
        behaviorContent.append("&params=").append(params);
        behaviorContent.append("&method=").append(request.getMethod());
        behaviorContent.append("&referer=").append(request.getHeader("Referer"));
        behaviorContent.append("&userAgent=").append(userAgent);
        behaviorContent.append("&os=").append(getOS(request));
        behaviorContent.append("&browser=").append(getBrowser(request));
        behaviorContent.append("&nodeId=").append("0");
        behaviorContent.append("&product=").append("photography");
        behaviorContent.append("&userAccount=").append(getUsername());
        behaviorContent.append("&userType=").append(getUsertype());
        
        
        Thread thread = new Thread(new SendUserBehavior(behaviorUrl.toString(),behaviorContent.toString()));
        thread.start();
    }

    /**
     * 记录异常错误
     * 格式 [exception]
     *
     * @param message
     * @param e
     */
    public static void logError(String message, Throwable e) {
        String username = getUsername();
        StringBuilder s = new StringBuilder();
        s.append(getBlock("exception"));
        s.append(getBlock(username));
        s.append(getBlock(message));
        ERROR_LOG.error(s.toString(), e);
    }
    public static void logSyncUserInfo(String message) {
      StringBuilder s = new StringBuilder();
      s.append(getBlock("SYNC_USER_LOG"));
      s.append(getBlock(message));
      SYNC_USER_LOG.info(s.toString());
    }
    public static void logSyncUserError(String message,Throwable e) {
      StringBuilder s = new StringBuilder();
      s.append(getBlock("SYNC_USER_ERROR"));
      s.append(getBlock(message));
      SYNC_USER_LOG.error(s.toString(),e);
    }
    public static void logSyncProductInfo(String message) {
    	StringBuilder s = new StringBuilder();
    	s.append(getBlock("SYNC_PRODUCT_LOG"));
    	s.append(getBlock(message));
    	SYNC_PRODUCT_LOG.info(s.toString());
    }
    public static void logSyncProductError(String message,Throwable e) {
    	StringBuilder s = new StringBuilder();
    	s.append(getBlock("SYNC_PRODUCT_ERROR"));
    	s.append(getBlock(message));
    	SYNC_PRODUCT_LOG.error(s.toString(),e);
    }
    public static void logSyncOperatorInfo(String message) {
      StringBuilder s = new StringBuilder();
      s.append(getBlock("SYNC_OPERATOR_LOG"));
      s.append(getBlock(message));
      SYNC_OPERATOR_LOG.info(s.toString());
    }
    public static void logSyncOperatorError(String message,Throwable e) {
      StringBuilder s = new StringBuilder();
      s.append(getBlock("SYNC_OPERATOR_ERROR"));
      s.append(getBlock(message));
      SYNC_OPERATOR_LOG.error(s.toString(),e);
    }
    public static void logSyncUserToGsbInfo(String message) {
        StringBuilder s = new StringBuilder();
        s.append(getBlock("SYNC_USERTOGSB_LOG"));
        s.append(getBlock(message));
        SYNC_USERTOGSB_LOG.info(s.toString());
    }
    public static void logSyncUserToGsbError(String message,Throwable e) {
        StringBuilder s = new StringBuilder();
        s.append(getBlock("SYNC_USERTOGSB_ERROR"));
        s.append(getBlock(message));
        SYNC_USERTOGSB_LOG.error(s.toString(),e);
    }
    /**
     * 记录页面错误
     * 错误日志记录 [page/eception][username][statusCode][errorMessage][servletName][uri][exceptionName][ip][exception]
     *
     * @param request
     */
    public static void logPageError(HttpServletRequest request) {
        String username = getUsername();

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String message = (String) request.getAttribute("javax.servlet.error.message");
        String uri = (String) request.getAttribute("javax.servlet.error.request_uri");
        Throwable t = (Throwable) request.getAttribute("javax.servlet.error.exception");


        if (statusCode == null) {
            statusCode = 0;
        }

        StringBuilder s = new StringBuilder();
        s.append(getBlock(t == null ? "page" : "exception"));
        s.append(getBlock(username));
        s.append(getBlock(statusCode));
        s.append(getBlock(message));
        s.append(getBlock(IpUtils.getIpAddr(request)));

        s.append(getBlock(uri));
        s.append(getBlock(request.getHeader("Referer")));
        StringWriter sw = new StringWriter();

        while (t != null) {
            t.printStackTrace(new PrintWriter(sw));
            t = t.getCause();
        }
        s.append(getBlock(sw.toString()));
        getErrorLog().error(s.toString());

    }


    public static String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }



    protected static String getParams(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        return JSON.toJSONString(params);
    }


    private static String getHeaders(HttpServletRequest request) {
        Map<String, List<String>> headers = Maps.newHashMap();
        Enumeration<String> namesEnumeration = request.getHeaderNames();
        while(namesEnumeration.hasMoreElements()) {
            String name = namesEnumeration.nextElement();
            Enumeration<String> valueEnumeration = request.getHeaders(name);
            List<String> values = Lists.newArrayList();
            while(valueEnumeration.hasMoreElements()) {
                values.add(valueEnumeration.nextElement());
            }
            headers.put(name, values);
        }
        return JSON.toJSONString(headers);
    }

    /**
     * @param userAgent
     * @return 客户端操浏览器
     */
    protected static String getBrowser(HttpServletRequest request) {
      String userAgent = request.getHeader("User-Agent");
      if (userAgent.indexOf("MSIE") > 0)
        return "IE";
      if (userAgent.indexOf("Firefox") > 0)
        return "Firefox";
      if (userAgent.indexOf("Opera") > 0)
        return "Opera";
      if (userAgent.indexOf("Chrome") > 0)
        return "Chrome";
      if (userAgent.indexOf("Safari") > 0)
        return "Safari";
      if (userAgent.indexOf("MQQBrowser") > 0)
        return "MQQBrowser";
      return "Unknow";
    }

    /**
     * @param userAgent
     * @return 客户端操作系统
     */
    protected static String getOS(HttpServletRequest request) {
      String userAgent = request.getHeader("User-Agent");
      if (userAgent.indexOf("Windows") > 0)
        return "Windows";
      if (userAgent.indexOf("Android") > 0)
        return "Android";
      if (userAgent.indexOf("Linux") > 0)
        return "Linux";
      if (userAgent.indexOf("iPhone") > 0)
        return "iPhone";
      if (userAgent.indexOf("iOS") > 0)
        return "iOS";
      if (userAgent.indexOf("SymbianOS") > 0)
        return "SymbianOS";
      if (userAgent.indexOf("OS X") > 0)
        return "OSX";
      return "Unknow";
    }
    protected static String getUsername() {
        return SecurityUtil.getLoginOperatorName();
    }
    protected static String getUsertype() {
      return SecurityUtil.getLoginOperatorType();
  }
    public static Logger getAccessLog() {
        return ACCESS_LOG;
    }

    public static Logger getErrorLog() {
        return ERROR_LOG;
    }

}

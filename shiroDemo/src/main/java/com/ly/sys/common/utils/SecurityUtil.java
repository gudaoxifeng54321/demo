package com.ly.sys.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ly.common.X;
import com.ly.common.bean.SpringUtils;
import com.ly.common.encrypt.AES;
import com.ly.sys.core.user.service.OperatorService;
import com.ly.sys.core.user.vo.Operator;

public class SecurityUtil {
    private static InheritableThreadLocal<Operator> userThreadLocal = new InheritableThreadLocal<Operator>();
    private static AES aes = SpringUtils.getBean("aes");
    private static OperatorService operatorService = SpringUtils.getBean("operatorService");
    
    public static  Long getLoginOperatorId(){
      HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
      Object _id = request.getSession().getAttribute(X.USER);
      if(null != _id){
        return Long.valueOf(_id.toString());
      }
      return null;
    }
    
    public static  Operator getLoginOperator(){
      HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
      Object _id = request.getSession().getAttribute(X.USER);
      //获取登录的用户
      
      Operator user = operatorService.findById(Long.valueOf(_id.toString()));
      if(null==user){
        return null;
      }
      return user;
    }
    public static  String getLoginOperatorName(){
      HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
      Object _name = request.getSession().getAttribute(X.USER_NAME);
      if(_name == null){
        return null;
      }
      return _name.toString();
    }
    
    public static  String getLoginOperatorType(){
      return "go2Operator";
    }
    public static void addUserInfo2Cookie(Long id,String username, HttpServletResponse response){
      Operator user = new Operator();
      user.setId(id);
      user.setUsername(username);
      addUserInfo2Cookie(user, response);
    }
    public static void addUserInfo2Cookie(Operator user,HttpServletResponse response){
      // 登录成功设置session 与 cookie
      
      String domains = X.getConfig("server.cookie.domains");
      String maxage = X.getConfig("server.cookie.maxage");
      int maxAge = -1;
      if(StringUtils.isNotEmpty(maxage)){
        maxAge = Integer.parseInt(maxage);
      }
      Cookie cuid = new Cookie(X.USER, X.USER.startsWith(X.ENCRYPTED) ? aes.encrypt(user.getId().toString()) : user.getId().toString());
      cuid.setMaxAge(maxAge);
      cuid.setPath(X.WEB_ROOT);
      

      Cookie cuname = new Cookie(X.USER_NAME, X.USER_NAME.startsWith(X.ENCRYPTED) ? aes.encrypt(user.getUsername()) : user.getUsername());
      cuname.setMaxAge(maxAge);
      cuname.setPath(X.WEB_ROOT);
      
      if(StringUtils.isNotEmpty(domains)){
        cuid.setDomain(domains);
        cuname.setDomain(domains);
      }
      response.addCookie(cuid);
      response.addCookie(cuname);
    }
    public static Operator getUserInfoFromCookie(HttpServletRequest request){
      // 检测userId和 userType
      Cookie[] clientCookiesArray = request.getCookies();
      if (null != clientCookiesArray) {
        String userId = "";
        String username = "";
        for (Cookie c : clientCookiesArray) {
          String name = c.getName();
          String value = name.startsWith(X.ENCRYPTED) ? aes.decrypt(c.getValue()) : c.getValue();
          if(name.equals(X.USER)){
            userId = value;
            continue;
          }
          if(name.equals(X.USER_NAME)){
            username = value;
            continue;
          }
        }
        if(StringUtils.isNotEmpty(userId)&&StringUtils.isNotEmpty(username)){
          Operator user = new Operator();
          user.setId(Long.valueOf(userId));
          user.setUsername(username);
          return user;
        }
      }
      return null;
    }
    public static void addUserInfo2Session(Long id,String username, HttpServletRequest request){
      Operator user = new Operator();
      user.setId(id);
      user.setUsername(username);
      addUserInfo2Session(user, request);
    }
    public static void addUserInfo2Session(Operator user,HttpServletRequest request){
      request.getSession(true).setAttribute(X.USER, user.getId().toString());
      request.getSession().setAttribute(X.USER_NAME, user.getUsername());
    }
    public static Operator getUserInfoFromSession(HttpServletRequest request){
      Object userId = request.getSession().getAttribute(X.USER);
      Object username = request.getSession().getAttribute(X.USER_NAME);
      if(userId != null && username != null){
        Operator user = new Operator();
        user.setId(Long.valueOf(userId.toString()));
        user.setUsername(username.toString());
        return user;
      }
      return null;
    }
    public static void removeUserinfoFromSession(HttpServletRequest request){
      request.getSession().removeAttribute(X.USER);
      request.getSession().removeAttribute(X.USER_NAME);
    }
    public static void removeUserinfoFromCookie(HttpServletResponse response){
      String domains = X.getConfig("server.cookie.domains");
      Cookie cuid = new Cookie(X.USER, "");
      cuid.setMaxAge(0);
      cuid.setPath(X.WEB_ROOT);
      

      Cookie cuname = new Cookie(X.USER_NAME, "");
      cuname.setMaxAge(0);
      cuname.setPath(X.WEB_ROOT);
      
      if(StringUtils.isNotEmpty(domains)){
        cuid.setDomain(domains);
        cuname.setDomain(domains);
      }
      response.addCookie(cuid);
      response.addCookie(cuname);
    }
}

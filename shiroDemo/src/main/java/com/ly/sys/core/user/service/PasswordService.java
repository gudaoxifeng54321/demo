package com.ly.sys.core.user.service;

import javax.annotation.PostConstruct;

/*import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ly.common.X;
import com.ly.common.encrypt.MD5;
import com.ly.sys.common.utils.UserLogUtils;
import com.ly.sys.core.user.exception.UserPasswordNotMatchException;
import com.ly.sys.core.user.exception.UserPasswordRetryLimitExceedException;
import com.ly.sys.core.user.vo.BaseUser;
@Service
public class PasswordService{
/*  @Autowired
  private CacheManager ehcacheManager;

  private Cache        loginRecordCache;*/
 /* @Value(value = "${user.password.maxRetryCount}")*/
  private int          maxRetryCount = 10;

  public void setMaxRetryCount(int maxRetryCount) {
    this.maxRetryCount = maxRetryCount;
  }

  
/*  @PostConstruct
  public void init() {
    loginRecordCache = ehcacheManager.getCache("loginRecordCache");
  }
*/
 
  public String encryptPassword(String username, String password) {
    String salt=X.getConfig("PASSWORD_SALT_db_go2");
    String pwd = salt + password;
    try {
      return (password == null) ? "" : MD5.md5Encode(pwd);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  
  public void validate(BaseUser baseUser, String password) {
   /* String username = baseUser.getUsername();
    int retryCount = 0;
    Element cacheElement = loginRecordCache.get(username);
    if (cacheElement != null) {
      retryCount = (Integer) cacheElement.getObjectValue();
      if (retryCount >= maxRetryCount) {
        UserLogUtils.log(username, "passwordError", "password error, retry limit exceed! password: {},max retry count {}", password,
            maxRetryCount);
        throw new UserPasswordRetryLimitExceedException(maxRetryCount);
      }
    }
    
    if (!matches(baseUser, password)) {
      loginRecordCache.put(new Element(username, ++retryCount));
      UserLogUtils.log(username, "passwordError", "password error! password: {} retry count: {}", password, retryCount);
      throw new UserPasswordNotMatchException();
    } else {
      clearLoginRecordCache(username);
    }*/
    
    

  }

  public boolean matches(BaseUser baseUser, String newPassword) {
    return baseUser.getPassword().equals(encryptPassword(baseUser.getUsername(), newPassword));
  }

  public void clearLoginRecordCache(String username) {
    /*loginRecordCache.remove(username);*/
  }
}

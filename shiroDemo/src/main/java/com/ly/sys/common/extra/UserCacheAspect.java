package com.ly.sys.common.extra;

import java.util.List;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.ly.sys.common.cache.BaseCacheAspect;
import com.ly.sys.common.entity.BaseEntity;
import com.ly.sys.core.user.vo.User;

/**
 * <p>Description: 用户缓存切面
 * <p>User: mtwu
 * <p>Date: 2016-11-7
 * <p>Version: 1.0
 */

@Component
@Aspect
public class UserCacheAspect extends BaseCacheAspect {
  
  public UserCacheAspect() {
    setCacheName("sys-userCache");
  }
  
  private String idKeyPrefix = "id-";
  private String usernameKeyPrefix = "username-";
  private String emailKeyPrefix = "email-";
  private String mobilePhoneNumberKeyPrefix = "mobilePhoneNumber-";
  
  ////////////////////////////////////////////////////////////////////////////////
  ////切入点
  ////////////////////////////////////////////////////////////////////////////////
  
  /**
  * 匹配用户Service
  */
  @Pointcut(value = "target(com.ly.sys.core.user.service.UserService)")
  private void userServicePointcut() {
  }
  
  
  /**
   * only put
   * 如 新增 修改
   */
  @Pointcut(value =
            "(execution(* add(..))&& args(arg)) " +
                    "|| (execution(* update(..))&& args(arg))" +
                    "|| (execution(* batchUpdate(..))&& args(arg))", argNames = "arg")
  @SuppressWarnings("unused")
    private void cachePutPointcut(Object arg) {
    }

  
  @After(value = "userServicePointcut() && cachePutPointcut(arg)", argNames = "arg")
  @SuppressWarnings("unchecked")
  public void cachePutAdvice(Object arg) {
    if (arg == null) {
        return;
    }
    //put cache
    if (arg instanceof BaseEntity) {
        put((User) arg);
    }
    if (arg instanceof List) {
      for (User user : (List<User>) arg) {
        put((User) user);
      }
  }
    
  }
  

  
  
  private String idKey(String id) {
      return idKeyPrefix + id;
  }
  
  private String usernameKey(String username) {
      return usernameKeyPrefix + username;
  }
  
    
  ////////////////////////////////////////////////////////////////////////////////
  ////cache 抽象实现
  ////////////////////////////////////////////////////////////////////////////////
  public void put(User user) {
      if (user == null) {
          return;
      }
      Long id = user.getId();
      put(usernameKey(user.getUsername()), id);
      // id ---> user
      put(idKey(String.valueOf(id)), user);
  }
  
  public void evictId(String id) {
      evict(idKey(id));
  }
  
  public void evict(User user) {
      if (user == null) {
          return;
      }
      Long id = user.getId();
      evict(idKey(String.valueOf(id)));
      evict(usernameKey(user.getUsername()));
  }
}

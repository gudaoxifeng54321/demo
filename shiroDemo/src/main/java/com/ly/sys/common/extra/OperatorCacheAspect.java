package com.ly.sys.common.extra;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ly.sys.common.cache.BaseCacheAspect;
import com.ly.sys.core.user.vo.Operator;
import com.ly.sys.core.user.vo.User;

/**
 * <p>
 * Description: 用户缓存切面
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-11-7
 * <p>
 * Version: 1.0
 */

@Component
@Aspect
public class OperatorCacheAspect extends BaseCacheAspect {

  public OperatorCacheAspect() {
    setCacheName("sys-operatorCache");
  }

  private String idKeyPrefix                = "id-";
  private String usernameKeyPrefix          = "username-";
  private String emailKeyPrefix             = "email-";
  private String mobilePhoneNumberKeyPrefix = "mobilePhoneNumber-";

  // //////////////////////////////////////////////////////////////////////////////
  // //切入点
  // //////////////////////////////////////////////////////////////////////////////

  /**
   * 匹配用户Service
   */
  @Pointcut(value = "target(com.ly.sys.core.user.service.OperatorService)")
  private void operatorServicePointcut() {
	  System.out.println("................");
  }

  /**
   * only put 如 新增 修改 登录 改密 改状态 只有涉及修改即可
   */
  @Pointcut(value = "execution(* add(..)) " + "|| execution(* update(..)) " + "|| execution(* login(..)) "
      + "|| execution(* changePassword(..)) " + "|| execution(* changeStatus(..))")
  @SuppressWarnings("unused")
  private void cachePutPointcut() {
  }

  /**
   * evict 比如删除
   */
  @Pointcut(value = "(execution(* delete(*))) && args(arg)", argNames = "arg")
  private void cacheEvictPointcut(Object arg) {
  }

  /**
   * put 或 get 比如查询
   */
  @Pointcut(value = "(execution(* findByUsername(*)) " + "|| execution(* findByEmail(*)) " + "|| execution(* findByMobilePhoneNumber(*)) "
      + "|| execution(* findById(*)))")
  private void cacheablePointcut() {
  }

  // //////////////////////////////////////////////////////////////////////////////
  // //增强实现
  // //////////////////////////////////////////////////////////////////////////////

  @AfterReturning(value = "operatorServicePointcut() && cachePutPointcut()", returning = "operator")
  public void cachePutAdvice(Object operator) {
    if (operator == null) {
      return;
    }
    // put cache
    put((Operator) operator);

  }

  @After(value = "operatorServicePointcut() && cacheEvictPointcut(arg)", argNames = "arg")
  public void cacheEvictAdvice(Object arg) {
    if (arg == null) {
      return;
    }
    if (arg instanceof Long) {
      // only evict id
      evictId(String.valueOf(arg));
    }
    if (arg instanceof Long[]) {
      for (Long id : (Long[]) arg) {
        // only evict id
        evictId(String.valueOf(id));
      }
    }

    if (arg instanceof String) {
      // only evict id
      evictId((String) arg);
    }
    if (arg instanceof String[]) {
      for (String id : (String[]) arg) {
        // only evict id
        evictId(String.valueOf(id));
      }
    }
    if (arg instanceof User) {
      // evict user
      evict((User) arg);
    }
  }
  
  @Around(value = "operatorServicePointcut() && cacheablePointcut()")
  public Object cacheableAdvice(ProceedingJoinPoint pjp) throws Throwable {
	  System.out.println("................................");
      String methodName = pjp.getSignature().getName();
      Object arg = pjp.getArgs().length >= 1 ? pjp.getArgs()[0] : null;

      String key = "";
      boolean isIdKey = false;
      if ("findById".equals(methodName)) {
          key = idKey(String.valueOf(arg));
          isIdKey = true;
      } else if ("findByUsername".equals(methodName)) {
          key = usernameKey((String) arg);
      } else if ("findByEmail".equals(methodName)) {
          key = emailKey((String) arg);
      } else if ("findByMobilePhoneNumber".equals(methodName)) {
          key = mobilePhoneNumberKey((String) arg);
      }

      Operator operator = null;
      if (isIdKey == true) {
        operator = get(key);
      } else {
          Long id = get(key);
          if (id != null) {
              key = idKey(String.valueOf(id));
              operator = get(key);
          }
      }
      //cache hit
      if (operator != null) {
          log.debug("cacheName:{}, hit key:{}", cacheName, key);
          return operator;
      }
      log.debug("cacheName:{}, miss key:{}", cacheName, key);

      //cache miss
      operator = (Operator) pjp.proceed();

      //put cache
      put(operator);
      return operator;

  }
  

  private String idKey(String id) {
    return idKeyPrefix + id;
  }

  private String usernameKey(String username) {
    return usernameKeyPrefix + username;
  }

  private String emailKey(String email) {
    return emailKeyPrefix + email;
  }

  private String mobilePhoneNumberKey(String mobilePhoneNumber) {
    return mobilePhoneNumberKeyPrefix + mobilePhoneNumber;
  }

  // //////////////////////////////////////////////////////////////////////////////
  // //cache 抽象实现
  // //////////////////////////////////////////////////////////////////////////////
  public void put(Operator operator) {
    if (operator == null) {
      return;
    }
    Long id = operator.getId();
    // username email mobilePhoneNumber ---> id
    put(usernameKey(operator.getUsername()), id);
    if (!StringUtils.isEmpty(operator.getEmail())) {
      put(emailKey(operator.getEmail()), id);
    }
    if (!StringUtils.isEmpty(operator.getMobile())) {
      put(mobilePhoneNumberKey(operator.getMobile()), id);
    }
    // id ---> user
    put(idKey(String.valueOf(id)), operator);
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
    evict(emailKey(user.getEmail()));
    evict(mobilePhoneNumberKey(user.getMobile()));
  }
}

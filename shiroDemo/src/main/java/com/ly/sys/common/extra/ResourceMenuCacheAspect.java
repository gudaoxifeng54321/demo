package com.ly.sys.common.extra;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.ly.sys.common.cache.BaseCacheAspect;
import com.ly.sys.core.user.vo.Operator;

/**
 * <p>缓存及清理菜单缓存
 * Description:
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-11-8
 * <p>
 * Version: 1.0
 */
@Component
@Aspect
public class ResourceMenuCacheAspect extends BaseCacheAspect {

    public ResourceMenuCacheAspect() {
        setCacheName("sys-menuCache");
    }

    private String menusKeyPrefix = "menus-";


    @Pointcut(value = "target(com.ly.sys.core.resource.service.ResourceService)")
    private void resourceServicePointcut() {
    }

    @Pointcut(value = "execution(* add(..)) || execution(* update(..)) || execution(* delete(..))")
    private void resourceCacheEvictAllPointcut() {
    }

    @Pointcut(value = "execution(* findMenus(*)) && args(arg)", argNames = "arg")
    private void resourceCacheablePointcut(Operator arg) {
    }

    @Before(value = "resourceServicePointcut() && resourceCacheEvictAllPointcut()")
    public void findRolesCacheableAdvice() throws Throwable {
        clear();
    }

    @Around(value = "resourceServicePointcut() && resourceCacheablePointcut(arg)", argNames = "pjp,arg")
    public Object findRolesCacheableAdvice(ProceedingJoinPoint pjp, Operator arg) throws Throwable {

      Operator operator = arg;

        String key = menusKey(operator.getId());
        Object retVal = get(key);

        if (retVal != null) {
            log.debug("cacheName:{}, method:findRolesCacheableAdvice, hit key:{}", cacheName, key);
            return retVal;
        }
        log.debug("cacheName:{}, method:findRolesCacheableAdvice, miss key:{}", cacheName, key);

        retVal = pjp.proceed();

        put(key, retVal);

        return retVal;
    }


    public void evict(Long operatorId) {
        evict(menusKey(operatorId));
    }


    private String menusKey(Long operatorId) {
        return this.menusKeyPrefix + operatorId;
    }


}

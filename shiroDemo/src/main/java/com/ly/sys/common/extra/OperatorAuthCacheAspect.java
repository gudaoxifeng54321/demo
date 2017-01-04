package com.ly.sys.common.extra;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ly.sys.common.cache.BaseCacheAspect;
import com.ly.sys.core.auth.service.AuthOperatorService;
import com.ly.sys.core.auth.vo.AuthOperator;
import com.ly.sys.core.permission.service.PermissionService;
import com.ly.sys.core.permission.service.RoleService;
import com.ly.sys.core.permission.vo.Permission;
import com.ly.sys.core.permission.vo.Role;
import com.ly.sys.core.resource.service.ResourceService;
import com.ly.sys.core.resource.vo.Resource;
import com.ly.sys.core.user.service.OperatorService;
import com.ly.sys.core.user.vo.Operator;

/**
 * <p>Description:
 * <p>User: mtwu
 * <p>Date: 2016-11-8
 * <p>Version: 1.0
 */
@Component
@Aspect
public class OperatorAuthCacheAspect extends BaseCacheAspect {

  public OperatorAuthCacheAspect() {
    setCacheName("sys-authCache");
  }

  private String                  rolesKeyPrefix             = "roles-";
  private String                  stringRolesKeyPrefix       = "string-roles-";
  private String                  stringPermissionsKeyPrefix = "string-permissions-";

  @Autowired
  private ResourceMenuCacheAspect resourceMenuCacheAspect;

  @Autowired
  private AuthOperatorService     authOperatorService;
  @Autowired
  private ResourceService         resourceService;
  @Autowired
  private PermissionService       permissionService;
  @Autowired
  private RoleService             roleService;
  @Autowired
  private OperatorService         operatorService;

  /**
   * 2、授权（Auth） 当增删改授权时，
   * 如果是用户相关的，
   * 只删用户的即可
   */
  @Pointcut(value = "target(com.ly.sys.core.auth.service.AuthOperatorService)")
  private void AuthOperatorService() {
  }

  @Pointcut(value = "(execution(* update(*)) && args(arg)) " + "|| (execution(* add(*)) && args(arg)) "
      + "|| (execution(* delete(*)) && args(arg))" + "|| (execution(* save(*)) && args(arg))", argNames = "arg")
  private void authCacheEvictAllOrSpecialPointcut(Object arg) {
  }

  /**
   * 3.1、资源（Resource）
   * 当修改资源时判断是否发生变化（如resourceIdentity，是否显示），如果变了清缓存
   * 当删除资源时，清缓存
   */
  @Pointcut(value = "target(com.ly.sys.core.resource.service.ResourceService)")
  private void resourceServicePointcut() {
  }

  @Pointcut(value = "execution(* delete(..))")
  private void resourceCacheEvictAllPointcut() {
  }

  @Pointcut(value = "execution(* update(*)) && args(arg)", argNames = "arg")
  private void resourceMaybeCacheEvictAllPointcut(Resource arg) {
  }

  /**
   * 3.2、权限（Permission）
   * 当修改权限时判断是否发生变化（如permission，是否显示），
   * 如果变了清缓存 当删除权限时，清缓存
   */
  @Pointcut(value = "target(com.ly.sys.core.permission.service.PermissionService)")
  private void permissionServicePointcut() {
  }

  @Pointcut(value = "execution(* delete(..))")
  private void permissionCacheEvictAllPointcut() {
  }

  @Pointcut(value = "execution(* update(*)) && args(arg)", argNames = "arg")
  private void permissionMaybeCacheEvictAllPointcut(Permission arg) {
  }
  
  

  /**
   * 4、角色（Role）
   * 当删除角色时，请缓存
   * 当修改角色show/role/resourcePermissions关系时，清缓存
   */
  @Pointcut(value = "target(com.ly.sys.core.permission.service.RoleService)")
  private void roleServicePointcut() {
  }

  @Pointcut(value = "execution(* delete(..))")
  private void roleCacheEvictAllPointcut() {
  }

  @Pointcut(value = "execution(* update(*)) && args(arg)", argNames = "arg")
  private void roleMaybeCacheEvictAllPointcut(Role arg) {
  }
  
  @Pointcut(value = "target(com.ly.sys.core.permission.service.Role2Resource2PermissionService)")
  private void role2Resource2PermissionServicePointcut() {
  }
  
  @Pointcut(value = "execution(* updateAuth(..)) || execution(* deleteByRoleResourc(..))")
  private void rolePermissionCacheEvictAllPointcut() {
  }

  /**
   * 5、用户
   * 修改时，仅需清自己的缓存
   */
  @Pointcut(value = "target(com.ly.sys.core.user.service.OperatorService)")
  private void operatorServicePointcut() {
  }

  @Pointcut(value = "execution(* delete(*)) && args(arg) || execution(* update(*)) && args(arg)", argNames = "arg")
  private void operatorCacheEvictSpecialPointcut(Object arg) {
  }

  @Pointcut(value = "target(com.ly.sys.core.auth.service.AuthService)")
  private void authServicePointcut() {
  }

  @Pointcut(value = "execution(* findRoles(*)) && args(arg)", argNames = "arg")
  private void cacheFindRolesPointcut(Operator arg) {
  }

  @Pointcut(value = "execution(* findStringRoles(*)) && args(arg)", argNames = "arg")
  private void cacheFindStringRolesPointcut(Operator arg) {
  }

  @Pointcut(value = "execution(* findStringPermissions(*)) && args(arg)", argNames = "arg")
  private void cacheFindStringPermissionsPointcut(Operator arg) {
  }

  // ////////////////////////////////////////////////////////////////////////////////
  // //增强
  // ////////////////////////////////////////////////////////////////////////////////

  // ////////////////////////////////////////////////////////////////////////////////
  // //查询时 查缓存/加缓存
  // ////////////////////////////////////////////////////////////////////////////////
  @Around(value = "authServicePointcut() && cacheFindRolesPointcut(arg)", argNames = "pjp,arg")
  public Object findRolesCacheableAdvice(ProceedingJoinPoint pjp, Operator arg) throws Throwable {
    Operator operator = arg;

    String key = null;
    if (operator != null) {
      key = rolesKey(operator.getId());
    }

    Object retVal = get(key);

    if (retVal != null) {
      log.debug("cacheName:{}, method:findRolesCacheableAdvice, hit key:{}", cacheName, key);
      return retVal;
    }

    log.debug("cacheName:{}, method:findRolesCacheableAdvice, miss key:{}", cacheName, key);

    retVal = pjp.proceed();

    this.put(key, retVal);

    return retVal;
  }

  @Around(value = "authServicePointcut() && cacheFindStringRolesPointcut(arg)", argNames = "pjp,arg")
  public Object findStringRolesCacheableAdvice(ProceedingJoinPoint pjp, Operator arg) throws Throwable {
    Operator operator = arg;

    String key = null;
    if (operator != null) {
      key = stringRolesKey(operator.getId());
    }

    Object retVal = get(key);

    if (retVal != null) {
      log.debug("cacheName:{}, method:findStringRolesCacheableAdvice, hit key:{}", cacheName, key);
      return retVal;
    }
    log.debug("cacheName:{}, method:findStringRolesCacheableAdvice, miss key:{}", cacheName, key);

    retVal = pjp.proceed();

    this.put(key, retVal);

    return retVal;
  }

  @Around(value = "authServicePointcut() && cacheFindStringPermissionsPointcut(arg)", argNames = "pjp,arg")
  public Object findStringPermissionsCacheableAdvice(ProceedingJoinPoint pjp, Operator arg) throws Throwable {
    Operator operator = arg;

    String key = stringPermissionsKey(operator.getId());

    Object retVal = get(key);

    if (retVal != null) {
      log.debug("cacheName:{}, method:findStringPermissionsCacheableAdvice, hit key:{}", cacheName, key);
      return retVal;
    }
    log.debug("cacheName:{}, method:findStringPermissionsCacheableAdvice, miss key:{}", cacheName, key);

    retVal = pjp.proceed();

    this.put(key, retVal);

    return retVal;
  }

  // ////////////////////////////////////////////////////////////////////////////////
  // //清空整个缓存
  // ////////////////////////////////////////////////////////////////////////////////
  
  
  @Before("(resourceServicePointcut() && resourceCacheEvictAllPointcut())")
  public void cacheClearAllAdviceByResource() {
    log.debug("cacheName:{}, method:cacheClearAllAdvice, cache clear", cacheName);
    clear();
  }
  @Before("(permissionServicePointcut() && permissionCacheEvictAllPointcut())")
  public void cacheClearAllAdviceByPermission() {
    log.debug("cacheName:{}, method:cacheClearAllAdvice, cache clear", cacheName);
    clear();
  }
  @Before("(roleServicePointcut() && roleCacheEvictAllPointcut())")
  public void cacheClearAllAdviceByRole() {
    log.debug("cacheName:{}, method:cacheClearAllAdvice, cache clear", cacheName);
    clear();
  }
  
  // ////////////////////////////////////////////////////////////////////////////////
  // //可能清空特定/全部缓存
  // ////////////////////////////////////////////////////////////////////////////////

  /**
   * @param auth
   * @return 如果清空所有返回true 否则false
   */
  private boolean evictWithAuth(AuthOperator auth) {
    boolean needEvictSpecail = auth != null && auth.getOperatorId() != null;
    if (needEvictSpecail) {
      Long operatorId = auth.getOperatorId();
      log.debug("cacheName:{}, evictWithAuth, evict operatorId:{}", cacheName, operatorId);
      evict(operatorId);
      return false;
    } else {
      log.debug("cacheName:{}, method:evictWithAuth, cache clear", cacheName);
      clear();
      return true;
    }
  }

  @Before(value = "AuthOperatorService() && authCacheEvictAllOrSpecialPointcut(arg)", argNames = "jp,arg")
  public void authCacheClearSpecialOrAllAdvice(JoinPoint jp, Object arg) {
    log.debug("cacheName:{}, method:authCacheClearSpecialOrAllAdvice begin", cacheName);
    String methodName = jp.getSignature().getName();
    if (arg instanceof AuthOperator) {// 只清除某个用户的即可
      AuthOperator auth = (AuthOperator) arg;

      log.debug("cacheName:{}, method:authCacheClearSpecialOrAllAdvice delegate to evictWithAuth", cacheName);
      evictWithAuth(auth);
    } else if ("delete".equals(methodName)) { // 删除方法
      if (arg instanceof Long) { // 删除单个
        Long authId = (Long) arg;
        AuthOperator auth = authOperatorService.findById(authId);

        log.debug("cacheName:{}, method:authCacheClearSpecialOrAllAdvice delegate to evictWithAuth", cacheName);
        evictWithAuth(auth);
      } else if (arg instanceof Long[]) { // 批量删除
        for (Long authId : ((Long[]) arg)) {
          AuthOperator auth = authOperatorService.findById(authId);

          log.debug("cacheName:{}, method:authCacheClearSpecialOrAllAdvice delegate to evictWithAuth", cacheName);
          if (evictWithAuth(auth)) {// 如果清空的是所有 直接返回
            return;
          }
        }
      }
    }
  }

  @Before(value = "resourceServicePointcut() && resourceMaybeCacheEvictAllPointcut(arg)", argNames = "arg")
  public void resourceMaybeCacheClearAllAdvice(Resource arg) {
    Resource resource = arg;
    if (resource == null || resource.getIdentity() == null) {
      return;
    }
    Resource dbResource = resourceService.findById(resource.getId());
    if (dbResource == null || dbResource.getIdentity() == null) {
      return;
    }

    // 只有当show/identity发生改变时才清理缓存
    if (!dbResource.getIsShow().equals(resource.getIsShow()) || !dbResource.getIdentity().equals(resource.getIdentity())) {

      log.debug("cacheName:{}, method:resourceMaybeCacheClearAllAdvice, cache clear", cacheName);
      clear();
    }
  }

  @Before(value = "permissionServicePointcut() && permissionMaybeCacheEvictAllPointcut(arg)", argNames = "arg")
  public void permissionMaybeCacheClearAllAdvice(Permission arg) {

    Permission permission = arg;
    if (permission == null) {
      return;
    }
    Permission dbPermission = permissionService.findById(permission.getId());
    if (dbPermission == null) {
      return;
    }

    // 只有当show/permission发生改变时才清理缓存
    if (!dbPermission.getIsShow().equals(permission.getIsShow()) || !dbPermission.getPermission().equals(permission.getPermission())) {

      log.debug("cacheName:{}, method:permissionMaybeCacheClearAllAdvice, cache clear", cacheName);
      clear();
    }
  }
  
  @Before("(role2Resource2PermissionServicePointcut() && rolePermissionCacheEvictAllPointcut())")
  public void cacheClearAllAdviceByRolePermission() {
    log.debug("cacheName:{}, method:cacheClearAllAdvice, cache clear", cacheName);
    clear();
  }
  //有上面这个切点，所以不需要一下切点，实际上做角色的权限修改是在 Role2Resource2PermissionService里做的
  //@Before(value = "roleServicePointcut() && roleMaybeCacheEvictAllPointcut(arg)", argNames = "arg")
  public void roleMaybeCacheClearAllAdvice(Role arg) {
    Role role = arg;
    if (role == null) {
      return;
    }
    Role dbRole = roleService.findById(role.getId());
    if (dbRole == null) {
      return;
    }

    // 只有当show/role发生改变时才清理缓存
    if (!dbRole.getIsShow().equals(role.getIsShow())
        || !dbRole.getRole().equals(role.getRole())
        || !(dbRole.getResource2Permissions().size() == role.getResource2Permissions().size() && dbRole.getResource2Permissions()
            .containsAll(role.getResource2Permissions()))) {

      log.debug("cacheName:{}, method:roleMaybeCacheClearAllAdvice, cache clear", cacheName);
      clear();
    }
  }

  // ////////////////////////////////////////////////////////////////////////////////
  // //缓存相关
  // ////////////////////////////////////////////////////////////////////////////////

  @Override
  public void clear() {
    super.clear();
    resourceMenuCacheAspect.clear();// 当权限过期 同时清理菜单的
  }

  public void evict(Long[] operatorIds) {
    for (Long operatorId : operatorIds) {
      evict(operatorId);
    }
  }

  public void evict(Long operatorId) {
    evict(rolesKey(operatorId));
    evict(stringRolesKey(operatorId));
    evict(stringPermissionsKey(operatorId));

    resourceMenuCacheAspect.evict(operatorId);// 当权限过期 同时清理菜单的
  }

  private String rolesKey(Long operatorId) {
    return this.rolesKeyPrefix + operatorId;
  }

  private String stringRolesKey(Long operatorId) {
    return this.stringRolesKeyPrefix + operatorId;
  }

  private String stringPermissionsKey(Long operatorId) {
    return this.stringPermissionsKeyPrefix + operatorId;
  }

}

package com.ly.sys.core.auth.service;

import java.util.Set;


import org.apache.commons.lang.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ly.sys.core.permission.service.PermissionService;
import com.ly.sys.core.permission.service.RoleService;
import com.ly.sys.core.permission.vo.Permission;
import com.ly.sys.core.permission.vo.Role;
import com.ly.sys.core.permission.vo.Role2Resource2Permission;
import com.ly.sys.core.resource.service.ResourceService;
import com.ly.sys.core.user.vo.Operator;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.ly.sys.core.resource.vo.Resource;

@Service
public class AuthService{

  @Autowired
  private RoleService       roleService;

  @Autowired
  private ResourceService   resourceService;
  @Autowired
  private PermissionService permissionService;

  public Set<Role> findRoles(Operator operator) {
    if (operator == null) {
      return Sets.newHashSet();
    }
    Long operatorId = operator.getId();
    Set<Role> roles = roleService.findRoleIdsByOperatorId(operatorId);
    return roles;
  }
                                      
  public Set<String> findStringRoles(Operator operator) {
    Set<Role> roles = ((AuthService) AopContext.currentProxy()).findRoles(operator);
    return Sets.newHashSet(Collections2.transform(roles, new Function<Role, String>() {
      @Override
      public String apply(Role input) {
        return input.getRole();
      }
    }));
  }

  /**
   * 根据角色获取 权限字符串 如sys:admin
   * 
   * @param user
   * @return
   */
  public Set<String> findStringPermissions(Operator operator) {
    Set<String> permissions = Sets.newHashSet();
    Set<Role> roles = ((AuthService) AopContext.currentProxy()).findRoles(operator);
    if (roles != null) {
      for (Role role : roles) {
        for (Role2Resource2Permission rrp : role.getResource2Permissions()) {
          Resource resource = resourceService.findById(rrp.getResourceId());

          String actualResourceIdentity = resourceService.findActualResourceIdentity(resource);

          // 不可用 即没查到 或者标识字符串不存在
          if (resource == null || StringUtils.isEmpty(actualResourceIdentity) || Boolean.FALSE.equals(resource.getIsShow())) {
            continue;
          }
          String permissionIds = rrp.getPermissionIds();
          String[] pids = permissionIds.split(",");
          for (String permissionId : pids) {
            Permission permission = permissionService.findById(Long.parseLong(permissionId));

            // 不可用
            if (permission == null || Boolean.FALSE.equals(permission.getIsShow())) {
              continue;
            }
            permissions.add(actualResourceIdentity + ":" + permission.getPermission());

          }
        }

      }
    }
    return permissions;
  }

}

package com.ly.sys.core.permission.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ly.sys.common.service.BaseService;
import com.ly.sys.core.auth.dao.AuthOperatorDao;
import com.ly.sys.core.auth.vo.AuthOperator;
import com.ly.sys.core.permission.dao.RoleDao;
import com.ly.sys.core.permission.vo.Role;

/**
 * <p>
 * Description: this is RoleServiceImpl
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-12 16:33:32
 * <p>
 * Version: 1.0
 */
@Service
public class RoleService extends BaseService<Role, Long> {
  private RoleDao getRoleDao() {
    return (RoleDao) baseDao;
  }
  @Autowired
  private AuthOperatorDao authOperatorDao;

  /**
   * 复制对象属性
   * 
   * @param adsiteorder
   * @param adsiteorderForm
   */
  public void copyProperties(Role dest, Role orig) {
    if (orig.getName() != null)
      dest.setName(orig.getName());
    if (orig.getRole() != null)
      dest.setRole(orig.getRole());
    if (orig.getDescription() != null)
      dest.setDescription(orig.getDescription());
    if (orig.getIsShow() != null)
      dest.setIsShow(orig.getIsShow());
    if (orig.getDeleted() != null)
      dest.setDeleted(orig.getDeleted());
  }

  /**
   * 获取可用的角色列表
   * 
   * @param roleIds
   * @return
   */
  public Set<Role> findShowRoles(Set<Long> roleIds) {

    Set<Role> roles = new HashSet<Role>();

    // 查询所有可用角色
    List<Role> list = getRoleDao().findList();
    for (Role role : list) {
      if ("1".equals(role.getIsShow()) && roleIds.contains(role.getId()) && "0".equals(role.getDeleted())) {
        roles.add(role);
      }
    }
    return roles;
  }

  public Set<Role> findRoleIdsByOperatorId(Long operatorId) {
    AuthOperator authOperator = authOperatorDao.findByOperatorId(operatorId);
    Set<Role> roles = new HashSet<>();
    if (authOperator != null) {
      String[] role_ids = authOperator.getRoleIds().split(",");
      Role role;
      for (String roleId : role_ids) {
        role = getRoleDao().findRole2Permissions(Long.parseLong(roleId));
        if (null != role) {
          roles.add(role);
        }
      }
    }

    return roles;
  }

}

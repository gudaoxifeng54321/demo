package com.ly.sys.core.permission.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.ly.sys.common.service.BaseService;
import com.ly.sys.core.permission.dao.Role2Resource2PermissionDao;
import com.ly.sys.core.permission.vo.Role2Resource2Permission;

/**
 * <p>
 * Description: this is Role2Resource2PermissionServiceImpl
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-12 16:33:32
 * <p>
 * Version: 1.0
 */
@Service
public class Role2Resource2PermissionService extends BaseService<Role2Resource2Permission, Long> {
  private Role2Resource2PermissionDao getRole2resource2permissionDao() {
    return (Role2Resource2PermissionDao) baseDao;
  }

  /**
   * 复制属性
   * 
   * @param adsiteorder
   * @param adsiteorderForm
   */
  public void copyProperties(Role2Resource2Permission dest, Role2Resource2Permission orig) {
    if (orig.getRoleId() != null)
      dest.setRoleId(orig.getRoleId());
    if (orig.getResourceId() != null)
      dest.setResourceId(orig.getResourceId());
    if (orig.getPermissionIds() != null)
      dest.setPermissionIds(orig.getPermissionIds());
  }

  /**
    * 
    */
  public void updateAuth(Role2Resource2Permission role2Resource2Permission, List<String> permissionIds) {

    String permissions = null;
    if (null == role2Resource2Permission) {
      return;
    }

    // 角色对应资源是否已经有权限数据.
    if (null == role2Resource2Permission.getId()) {
      Map<String, Object> queryMap = new HashMap<String, Object>();
      queryMap.put("roleId", role2Resource2Permission.getRoleId());
      queryMap.put("resourceId", role2Resource2Permission.getResourceId());
      Role2Resource2Permission tempRole2Resource2Permission = getRole2resource2permissionDao().findRole2Resource2Permission(queryMap);
      if (null != tempRole2Resource2Permission) {
        role2Resource2Permission = tempRole2Resource2Permission;
      }
    }

    // 无权限,删除权限数据
    if (null == permissionIds || permissionIds.size() <= 0) {
      getRole2resource2permissionDao().delete(role2Resource2Permission.getId());
    } else {

      for (int i = 0; i < permissionIds.size(); i++) {
        if (i == 0) {
          permissions = permissionIds.get(i);
        } else {
          permissions += ("," + permissionIds.get(i));
        }
      }
      if (permissionIds.contains("1")) {
        permissions = "1";
      }

      role2Resource2Permission.setPermissionIds(permissions);
      if (null == role2Resource2Permission.getId()) {
        getRole2resource2permissionDao().insert(role2Resource2Permission);
      } else {
        getRole2resource2permissionDao().update(role2Resource2Permission);
      }
    }

  }
  
  public Role2Resource2Permission getRole2Resource2Permission(Long roleID,Long resourceID){
    Map<String, Object> queryMap = new HashMap<String, Object>();
    queryMap.put("roleId", roleID);
    queryMap.put("resourceId", resourceID);

    return getRole2resource2permissionDao().findRole2Resource2Permission(queryMap);
  }
  
  
  public void deleteByRoleResourc(Long roleId,Long[] resourceId){
    getRole2resource2permissionDao().deleteByRoleResourc(roleId, resourceId);
  }
  
  public void deleteByRole(@Param("roleId") Long roleId){
    getRole2resource2permissionDao().deleteByRole(roleId);
  }
  
}

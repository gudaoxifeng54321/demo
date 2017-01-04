package com.ly.sys.core.permission.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ly.sys.common.dao.BaseDao;
import com.ly.sys.core.permission.vo.Role2Resource2Permission;

/**
 * <p>
 * Description: this is Role2Resource2PermissionDao
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-12 16:33:32
 * <p>
 * Version: 1.0
 */
@Repository
public interface Role2Resource2PermissionDao extends BaseDao<Role2Resource2Permission, Long> {

  Role2Resource2Permission findRole2Resource2Permission(Map<String, Object> queryMap);
  
  public void deleteByRoleResourc(@Param("roleId") Long roleId,@Param("resourceIds") Long[] resourceId);
  
  /**
   * 删除角色所有资源权限
   * @param roleId
   */
  public void deleteByRole(@Param("roleId") Long roleId);
}

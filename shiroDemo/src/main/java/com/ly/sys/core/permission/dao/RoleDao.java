package com.ly.sys.core.permission.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ly.sys.common.dao.BaseDao;
import com.ly.sys.core.permission.vo.Role;

/**
 * <p>
 * Description: this is RoleDao
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-12 16:33:32
 * <p>
 * Version: 1.0
 */
@Repository
public interface RoleDao extends BaseDao<Role, Long> {

  /**
   * 找到角色、资源映射关系
   * 
   * @param parseLong
   * @return
   */
  public Role findRole2Permissions(@Param("id") long id);
}

package com.ly.sys.core.permission.service;

import org.springframework.stereotype.Service;

import com.ly.sys.common.service.BaseService;
import com.ly.sys.core.permission.dao.Resource2PermissionDao;
import com.ly.sys.core.permission.vo.Resource2Permission;

/**
 * <p>
 * Description: this is Resource2PermissionServiceImpl
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-12 16:33:32
 * <p>
 * Version: 1.0
 */
@Service
public class Resource2PermissionService extends BaseService<Resource2Permission, Long> {
  
  private Resource2PermissionDao getResource2permissionDao() {
    return (Resource2PermissionDao) baseDao;
  }

  /**
   * 复制对象属性
   * 
   * @param adsiteorder
   * @param adsiteorderForm
   */
  public void copyProperties(Resource2Permission dest, Resource2Permission orig) {
    if (orig.getResourceId() != null)
      dest.setResourceId(orig.getResourceId());
    if (orig.getPermissionId() != null)
      dest.setPermissionId(orig.getPermissionId());
  }

}

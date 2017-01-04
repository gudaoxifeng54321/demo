package com.ly.sys.core.permission.service;


import org.springframework.stereotype.Service;

import com.ly.sys.common.service.BaseService;
import com.ly.sys.core.permission.dao.PermissionDao;
import com.ly.sys.core.permission.vo.Permission;

 /**
 * <p>Description: this is Permission Service
 * <p>User: mtwu
 * <p>Date: 2016-6-12 16:33:31
 * <p>Version: 1.0
 */
@Service
public class PermissionService extends BaseService<Permission,Long>{
  

  private PermissionDao getPermissionDao() {
    return (PermissionDao) baseDao;
  }
  @Override
  public void copyProperties(Permission dest, Permission orig) {
    if(orig.getName() != null) dest.setName(orig.getName());
    if(orig.getPermission() != null) dest.setPermission(orig.getPermission());
    if(orig.getDescription() != null) dest.setDescription(orig.getDescription());
    if(orig.getIsShow() != null) dest.setIsShow(orig.getIsShow());
    if(orig.getDeleted() != null) dest.setDeleted(orig.getDeleted());
  }


}


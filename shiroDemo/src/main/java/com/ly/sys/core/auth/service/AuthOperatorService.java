package com.ly.sys.core.auth.service;

import org.springframework.stereotype.Service;

import com.ly.sys.common.service.BaseService;
import com.ly.sys.core.auth.dao.AuthOperatorDao;
import com.ly.sys.core.auth.vo.AuthOperator;

/**
 * <p>
 * Description: this is AuthOperatorServiceImpl
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-12 16:33:32
 * <p>
 * Version: 1.0
 */
@Service
public class AuthOperatorService extends BaseService<AuthOperator, Long> {
  private AuthOperatorDao getAuthoperatorDao() {
    return (AuthOperatorDao) baseDao;
  }

  /**
   * 复制对象属性
   * 
   * @param adsiteorder
   * @param adsiteorderForm
   */
  public void copyProperties(AuthOperator dest, AuthOperator orig) {
    if (orig.getOperatorId() != null)
      dest.setOperatorId(orig.getOperatorId());
    if (orig.getRoleIds() != null)
      dest.setRoleIds(orig.getRoleIds());
  }
  
  public AuthOperator findByOperatorId(Long operatorId){
    
    return getAuthoperatorDao().findByOperatorId(operatorId);
  }
}

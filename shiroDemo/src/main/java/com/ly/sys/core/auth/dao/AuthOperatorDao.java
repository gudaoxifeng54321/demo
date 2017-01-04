package com.ly.sys.core.auth.dao;

import org.springframework.stereotype.Repository;

import com.ly.sys.common.dao.BaseDao;
import com.ly.sys.core.auth.vo.AuthOperator;

 /**
 * <p>Description: this is AuthOperatorDao
 * <p>User: mtwu
 * <p>Date: 2016-6-12 16:33:32
 * <p>Version: 1.0
 */
@Repository
public interface AuthOperatorDao  extends BaseDao<AuthOperator, Long>{
	
  
  public AuthOperator findByOperatorId(Long operatorId);
}

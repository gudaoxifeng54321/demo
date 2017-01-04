package com.ly.sys.core.user.dao;

import org.springframework.stereotype.Repository;

import com.ly.sys.common.dao.BaseDao;
import com.ly.sys.core.user.vo.OperatorLastOnline;

 /**
 * <p>Description: this is OperatorLastOnlineDao
 * <p>User: mtwu
 * <p>Date: 2016-6-15 15:07:01
 * <p>Version: 1.0
 */
@Repository
public interface OperatorLastOnlineDao extends BaseDao<OperatorLastOnline,Long>{
	
	

    
    public OperatorLastOnline findByOperatorId(Long operatorId);
    
   	
}

package com.ly.sys.core.user.dao;

import org.springframework.stereotype.Repository;

import com.ly.sys.common.dao.BaseDao;
import com.ly.sys.core.user.vo.UserLastOnline;

 /**
 * <p>Description: this is UserLastOnlineDao
 * <p>User: mtwu
 * <p>Date: 2016-6-12 16:33:31
 * <p>Version: 1.0
 */
@Repository
public interface UserLastOnlineDao  extends BaseDao<UserLastOnline,Long>{
	
	
    
    /**
     * get one UserLastOnline information
     * @param userId
     * @return UserLastOnline
     */
     public UserLastOnline findByUserId(Long userId);
    
   	
}

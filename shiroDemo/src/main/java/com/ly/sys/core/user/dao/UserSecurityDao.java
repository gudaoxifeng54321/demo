package com.ly.sys.core.user.dao;

import org.springframework.stereotype.Repository;

import com.ly.sys.common.dao.BaseDao;
import com.ly.sys.core.user.vo.UserSecurity;

 /**
 * <p>Description: this is UserSecurityDao
 * <p>User: mtwu
 * <p>Date: 2016-6-12 16:33:31
 * <p>Version: 1.0
 */
@Repository
public interface UserSecurityDao extends BaseDao<UserSecurity,Long>{
	
	
    
    /**
     * 根据userID获取密保问题
     * @param userId
     * @return
     */
    public UserSecurity findByUserId(Long userId);
    
   	
}

package com.ly.sys.core.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ly.sys.common.dao.BaseDao;
import com.ly.sys.core.user.vo.User;

 /**
 * <p>Description: this is UserDao
 * <p>User: mtwu
 * <p>Date: 2016-6-12 16:33:30
 * <p>Version: 1.0
 */
@Repository
public interface UserDao  extends BaseDao<User,Long>{

	
   public List<User> check(@Param("param") String param, @Param("field") String field, @Param("source") String source);

   public List<User> findUser(@Param("username") String username, @Param("password") String password,@Param("source")String source);
   
   public int updateUser(User user);
   
   public int updateUserMobileApprove(@Param("id")Long id,@Param("mobile")String mobile);
   
   public List<User> findUserByIds(List<Integer> ids);
   
   public void batchInsert(List<User> list);
   
   public void batchUpdate(List<User> list);
   
   public User findByThirdIdAndSource(@Param("thirdId")Long thirdId,@Param("source") String source);
}

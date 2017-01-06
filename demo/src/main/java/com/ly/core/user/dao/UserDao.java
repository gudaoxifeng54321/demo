package com.ly.core.user.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ly.common.mvc.DAO;
import com.ly.core.user.vo.User;

 /**
 * <p>Description: this is UserDao
 * <p>User: mtwu
 * <p>Date: 2016-12-29 14:02:27
 * <p>Version: 1.0
 */
@Repository
public interface UserDao  extends DAO{
		
	/**
	 * 保存用户
	 * @param u
	 * @return
	 */
	int insert(User u);	
	
	User findById(Integer id);
	}

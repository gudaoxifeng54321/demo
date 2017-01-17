package com.ly.blog.core.user.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ly.blog.core.user.dao.UserDao;
import com.ly.blog.core.user.vo.User;


 /**
 * <p>Description: this is UserService
 * <p>User: mtwus
 * <p>Date: 2016-12-29 14:02:27
 * <p>Version: 1.0
 */
@Service
public class UserService{
@Autowired
private UserDao userDao;
	
		


	int save(User u){
		return userDao.insert(u);
	}
	
	/**
   * 使用BeanUtils的成本惊人地昂贵,手动写是最效率的，缺点增加了代码长度和阅读代码的难度
   * @param adsiteorder
   * @param adsiteorderForm
   */
   public void copyProperties(User dest,User orig){
			if(orig.getUsername() != null) dest.setUsername(orig.getUsername());
			if(orig.getPassword() != null) dest.setPassword(orig.getPassword());
		 }
	
}

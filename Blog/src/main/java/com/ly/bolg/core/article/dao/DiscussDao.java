package com.ly.bolg.core.article.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ly.bolg.core.article.vo.Discuss;
import com.ly.common.mvc.DAO;

 /**
 * <p>Description: this is DiscussDao
 * <p>User: mtwu
 * <p>Date: 2016-12-30 15:00:14
 * <p>Version: 1.0
 */
@Repository
public interface DiscussDao  extends DAO{
	
	int insert(Discuss discuss);
	
	List<Discuss> findPageList();
	
	}

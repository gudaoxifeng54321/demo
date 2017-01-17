package com.ly.bolg.core.article.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.bolg.core.article.dao.DiscussDao;
import com.ly.bolg.core.article.vo.Discuss;
import com.ly.common.mvc.Pagination;


 /**
 * <p>Description: this is DiscussService
 * <p>User: mtwus
 * <p>Date: 2016-12-30 15:00:14
 * <p>Version: 1.0
 */
@Service
public class DiscussService{
	@Autowired
	private DiscussDao discussDao;
	
	public PageInfo<Discuss> findPageList(Pagination pagination){
		PageHelper.startPage(pagination.getPageNum(), pagination.getPageSize());
		List<Discuss> list = discussDao.findPageList();
		PageInfo<Discuss> pageInfo = new PageInfo<Discuss>(list,pagination.getNavigationSize());
	    return pageInfo;
	}

	/**
   * 使用BeanUtils的成本惊人地昂贵,手动写是最效率的，缺点增加了代码长度和阅读代码的难度
   * @param adsiteorder
   * @param adsiteorderForm
   */
   public void copyProperties(Discuss dest,Discuss orig){
   												if(orig.getContent() != null) dest.setContent(orig.getContent());
						   }
	
}

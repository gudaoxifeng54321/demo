package com.ly.blog.core.user.dao;

import java.util.List;

import javax.annotation.Resource;

import com.ly.blog.core.user.vo.Score;
import com.ly.common.mvc.DAO;

@Resource
public interface ScoreDao extends DAO{
	List<Score> list();
	int save(Score s);
	void remove(Integer id);
	int update(Score s);
}

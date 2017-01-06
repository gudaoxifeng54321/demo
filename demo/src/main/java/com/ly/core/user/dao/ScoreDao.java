package com.ly.core.user.dao;

import java.util.List;

import javax.annotation.Resource;

import com.ly.common.mvc.DAO;
import com.ly.core.user.vo.Score;

@Resource
public interface ScoreDao extends DAO{
	List<Score> list();
	int save(Score s);
	void remove(Integer id);
	int update(Score s);
}

package com.ly.core.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ly.core.user.dao.ScoreDao;
import com.ly.core.user.vo.Score;
@Service
public class ScoreService {
	
	@Autowired
	private ScoreDao scoreDao;
	
	public List<Score> list() {
		return scoreDao.list();
	}
}

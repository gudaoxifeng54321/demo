package com.ly.bolg.core.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ly.bolg.core.user.service.ScoreService;
import com.ly.bolg.core.user.vo.Score;
import com.ly.common.mvc.BaseController;

@Controller
public class IndexController extends BaseController{
	@Autowired
	private ScoreService scoreService;
	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		return "index";
	}
	
	@RequestMapping("/json")
	@ResponseBody
	public String json(HttpServletRequest request) {
		return "jsonTest";
	}
	
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		List<Score> list = scoreService.list();
		for (Score score : list) {
			System.out.println(score.getName());
		}
		return "index";
	}

}

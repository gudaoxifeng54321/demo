package com.ly.blog.core.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ly.common.mvc.BaseController;

@Controller
public class PortalController extends BaseController{

	@RequestMapping("")
	public ModelAndView index() {
		return createModelAndView("main/index.html");
	}
	
	
	
	@RequestMapping("/indexJson")
	@ResponseBody
	public String indexJson() {
		return "index";
	}
}

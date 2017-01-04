package com.ly.sys.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ly.common.mvc.BaseVerificationController;
import com.ly.common.mvc.Box;
import com.ly.sys.common.Constants;
import com.ly.sys.core.resource.service.ResourceService;
import com.ly.sys.core.resource.vo.temp.Menu;
import com.ly.sys.core.user.vo.Operator;

/**
 * <p>
 * Description:
 * <p>
 * User: lujian
 * <p>
 * Date: 2016-06-20
 * <p>
 * Version: 1.0
 */
@Controller
@RequestMapping(value = "/admin")
public class IndexController extends BaseVerificationController {

  private final static Logger logger = LoggerFactory.getLogger(IndexController.class);

  @Autowired
  private ResourceService     resourceService;

  /**
   * 首页入口
   * 
   * @param request
   * @return
   */
  @RequestMapping(value = "/index")
  public ModelAndView loginTest(HttpServletRequest request, HttpServletResponse response) {
    logger.info("AuthUserController.login");

    Box box = loadNewBox(request);

    // 获取当前操作员对应的权限菜单
    Operator operator = (Operator) request.getAttribute(Constants.CURRENT_USER);

    List<Menu> list = resourceService.findMenus(operator);
    box.setAttribute("menus", list);
    return createModelAndView("/admin/index", box);
  }

  @RequestMapping(value = "/welcome")
  public ModelAndView welcome(HttpServletRequest request, HttpServletResponse response) {
    Box box = loadNewBox(request);
    // 最近3天的日历
    // model.addAttribute("calendarCount", calendarService.countRecentlyCalendar(loginOperator.getId(), 2));
    return createModelAndView("admin/welcome");
  }

}

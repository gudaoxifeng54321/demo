package com.ly.sys.core.resource.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ly.sys.common.Constants;
import com.ly.sys.common.web.controller.BaseTreeableController;
import com.ly.sys.core.resource.vo.Resource;

/**
 * Description: this is Resource Controller
 * 
 * @author mtwu
 * @version 1.0 2011
 */
@Controller
@RequestMapping(value = "/admin/sys/resource")
public class ResourceController extends BaseTreeableController<Resource, Long> {
  public ResourceController() {
    setResourceIdentity("sys:sysmgt:resource");
  }

  @RequestMapping(value = "/changeStatus/{newStatus}")
  public String changeStatus(HttpServletRequest request, @PathVariable("newStatus") Integer newStatus, @RequestParam("ids") Long[] ids,
      RedirectAttributes redirectAttributes) {

    this.permissionList.assertHasUpdatePermission();

    for (Long id : ids) {
      Resource resource = baseService.findById(id);
      resource.setIsShow(newStatus);
      baseService.update(resource);
    }

    redirectAttributes.addFlashAttribute(Constants.MESSAGE, "操作成功！");

    return "redirect:" + request.getAttribute(Constants.BACK_URL);
  }
  
  @RequestMapping(value = "ajax/{parent}/appendChild/discard", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public Object ajaxAppendChild(HttpServletRequest request, @PathVariable("parent") Long parentId) {
    throw new RuntimeException("discard method");
  }
  @RequestMapping(value = "ajax/{parent}/appendChild", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public Object ajaxAppendChildNew(HttpServletRequest request, @PathVariable("parent") Long parentId) {

    if (permissionList != null) {
      permissionList.assertHasCreatePermission();
    }

    Resource m = baseService.findById(parentId);
    Resource child = newModel();
    child.setName("新节点");
    child.setSource(m.getSource());
    baseService.appendChild(m, child);
    return convertToZtree(child, true, true);
  }

}

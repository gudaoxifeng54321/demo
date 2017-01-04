package com.ly.sys.core.permission.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ly.sys.common.Constants;
import com.ly.sys.common.enums.AvailableEnum;
import com.ly.sys.common.web.controller.BaseCRUDController;
import com.ly.sys.core.permission.vo.Permission;

/**
 * <p>
 * User: mtwu
 * <p>
 * Date: 13-1-28 下午4:29
 * <p>
 * Version: 1.0
 */
@Controller
@RequestMapping(value = "/admin/sys/permission/permission")
public class PermissionController extends BaseCRUDController<Permission, Long> {

  public PermissionController() {
    setResourceIdentity("sys:sysmgt:permission:permissionlist");
  }

  @Override
  protected void setCommonData(Model model) {
    super.setCommonData(model);
    model.addAttribute("availableList", AvailableEnum.values());
  }

  @RequestMapping(value = "/changeStatus/{newStatus}")
  public String changeStatus(HttpServletRequest request, @PathVariable("newStatus") Integer newStatus, @RequestParam("ids") Long[] ids) {

    this.permissionList.assertHasUpdatePermission();

    for (Long id : ids) {
      Permission permission = baseService.findById(id);
      permission.setIsShow(newStatus);
      baseService.update(permission);
    }

    return "redirect:" + request.getAttribute(Constants.BACK_URL);
  }

}

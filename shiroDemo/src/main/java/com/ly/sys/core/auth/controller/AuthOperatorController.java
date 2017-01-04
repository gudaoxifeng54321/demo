package com.ly.sys.core.auth.controller;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ly.sys.common.Constants;
import com.ly.sys.common.entity.search.Searchable;
import com.ly.sys.common.enums.AvailableEnum;
import com.ly.sys.common.utils.StringJoinUtils;
import com.ly.sys.common.web.controller.BaseCRUDController;
import com.ly.sys.core.auth.service.AuthOperatorService;
import com.ly.sys.core.auth.vo.AuthOperator;
import com.ly.sys.core.permission.service.RoleService;
import com.ly.sys.core.user.service.OperatorService;
import com.ly.sys.core.user.vo.Operator;

/**
 * Description: this is AuthOperator Controller
 * 
 * @author mtwu
 * @version 1.0 2011
 */
@Controller
@RequestMapping("/admin/sys/auth")
public class AuthOperatorController extends BaseCRUDController<AuthOperator, Long> {
  @Resource
  private RoleService roleService;
  @Resource
  private OperatorService operatorService;
  @Resource
  private AuthOperatorService authOperatorService;

  public AuthOperatorController() {
    setResourceIdentity("sys:sysmgt:permission:auth");
  }

  @Override
  protected void setCommonData(Model model) {
    super.setCommonData(model);
    model.addAttribute("availableList", AvailableEnum.values());
    Searchable searchable = Searchable.newSearchable();
     model.addAttribute("roles", roleService.findList(searchable));
     model.addAttribute("type", "user");
  }

  @RequestMapping(value = "/changeStatus/{newStatus}")
  public String changeStatus(HttpServletRequest request, @PathVariable("newStatus") Integer newStatus, @RequestParam("ids") Long[] ids) {

    this.permissionList.assertHasUpdatePermission();

    for (Long id : ids) {
      AuthOperator authOperator = baseService.findById(id);
      // authOperator.setIsShow(newStatus);
      baseService.update(authOperator);
    }

    return "redirect:" + request.getAttribute(Constants.BACK_URL); 
  }
  
  @RequestMapping(value = "{type}/create", method = RequestMethod.GET)
  public String showCreateFormWithType( Model model) {
      AuthOperator auth = new AuthOperator();
//      auth.setType(type);
      model.addAttribute("m", auth);
      return super.showCreateForm(model);
  }


  @RequestMapping(value = "create/discarded", method = RequestMethod.POST)
  public String create(
          Model model, @Valid @ModelAttribute("m") AuthOperator m, BindingResult result,
          RedirectAttributes redirectAttributes) {
      throw new RuntimeException("discard method");
  }

  
  
  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public String createWithType(
          Model model,
          @RequestParam(value = "userIds", required = false) String[] operatorNames,
          @Valid @ModelAttribute("m") AuthOperator m, BindingResult result,
          RedirectAttributes redirectAttributes,HttpServletRequest request) {

      this.permissionList.assertHasCreatePermission();

      if (hasError(m, result)) {
          return showCreateForm(model);
      }

      for(String str : operatorNames){ 
        AuthOperator authOperator=null;
        Operator operator = operatorService.findByUsername(str);
        if(null == operator){
          redirectAttributes.addFlashAttribute(Constants.MESSAGE, "新增失败");
          return redirectToUrl("/admin/sys/auth?search.type_eq=user" );
        }
        //是否已经存在授权
        authOperator = authOperatorService.findByOperatorId(operator.getId());
       if(null == authOperator){
         
         authOperator = new AuthOperator();
         authOperator.setOperatorId(operator.getId());
         authOperator.setRoleIds(m.getRoleIds());
       } else{
         //合并角色权限
         String[] oldRoles =  authOperator.getRoleIds().split(",");
         Set<String> totleRoles = new HashSet<String>();
         
         StringJoinUtils.SetAddArray(totleRoles, oldRoles);
         StringJoinUtils.SetAddArray(totleRoles, m.getRoleIds().split(","));
         authOperator.setRoleIds(StringJoinUtils.join(",", totleRoles));
       }
       baseService.save(authOperator);
      }

      redirectAttributes.addFlashAttribute(Constants.MESSAGE, "新增成功");
      return redirectToUrl("/admin/sys/auth?search.type_eq=user" );
  }
  
}

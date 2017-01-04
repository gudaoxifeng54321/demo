package com.ly.sys.core.permission.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Sets;
import com.ly.sys.common.enums.AvailableEnum;
import com.ly.sys.common.Constants;
import com.ly.sys.common.entity.search.Searchable;
import com.ly.sys.common.utils.StringJoinUtils;
import com.ly.sys.common.web.controller.BaseCRUDController;
import com.ly.sys.core.permission.service.PermissionService;
import com.ly.sys.core.permission.service.Role2Resource2PermissionService;
import com.ly.sys.core.permission.service.RoleService;
import com.ly.sys.core.permission.vo.Role;
import com.ly.sys.core.permission.vo.Role2Resource2Permission;

/**
 * Description: this is Role Controller
 * 
 * @author mtwu
 * @version 1.0 2011
 */
@Controller
@RequestMapping("/admin/sys/permission/role")
public class RoleController extends BaseCRUDController<Role, Long> {
  @Autowired
  private PermissionService permissionService;
  @Autowired
  private RoleService       roleService;
  @Autowired
  private Role2Resource2PermissionService role2Resource2PermissionService;
  public RoleController() {
    setResourceIdentity("sys:sysmgt:permission:rolelist");
  }

  @Override
  protected void setCommonData(Model model) {
    super.setCommonData(model);
    model.addAttribute("availableList", AvailableEnum.values());
    Searchable searchable = Searchable.newSearchable();
    model.addAttribute("permissions", permissionService.findList(searchable));
  }
  @RequestMapping(value = "{id}/update/discard", method = RequestMethod.POST)
  
  public String update(
          Model model, @Valid @ModelAttribute("m") Role m, BindingResult result,
          @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
          RedirectAttributes redirectAttributes) {

      throw new RuntimeException("discarded method");
  }
  @RequestMapping(value = "/changeStatus/{newStatus}")
  public String changeStatus(HttpServletRequest request, @PathVariable("newStatus") Integer newStatus, @RequestParam("ids") Long[] ids) {

    this.permissionList.assertHasUpdatePermission();

    for (Long id : ids) {
      Role role = baseService.findById(id);
      role.setIsShow(newStatus);
      baseService.update(role);
    }

    return "redirect:" + request.getAttribute(Constants.BACK_URL);
  }

  @RequestMapping("{role}/permissions")
  public String permissions(@PathVariable("role") Long roleId, Model model) {
    Role role = roleService.findById(roleId);
    model.addAttribute("role", role);
    return viewName("permissionsTable");
  }
  
  @RequestMapping(value = "create/discard", method = RequestMethod.POST)
  @Override
  public String create(
          Model model, @Valid @ModelAttribute("m") Role m, BindingResult result,
          RedirectAttributes redirectAttributes) {
      throw new RuntimeException("discarded method");
  }

  
  
  
  /**
   * 更新角色（包括角色对应资源权限）
   * @param model
   * @param role
   * @param result
   * @param resourceIds  所有资源ID
   * @param permissionIds 对应资源 所有权限
   * @param backURL
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
  public String updateWithResourcePermission(
          Model model,
          @Valid @ModelAttribute("m") Role role, BindingResult result,
          @RequestParam(value = "resourceId",required=false) Long[] resourceIds,
          @RequestParam(value = "permissionIds",required=false) Long[][] permissionIds,
          @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
          RedirectAttributes redirectAttributes) {
      fillResourcePermission(role, resourceIds, permissionIds);

      return super.update(model,role, result, backURL, redirectAttributes);
  }
  
  

  /**
   * 填充角色对应的资源权限
   * @param role
   * @param resourceIds
   * @param permissionIds
   */
  private void fillResourcePermission(Role role, Long[] resourceIds, Long[][] permissionIds) {
      

      //删除剩余资源权限
      role2Resource2PermissionService.deleteByRoleResourc(role.getId(), resourceIds);
      List<Role2Resource2Permission> resource2Permissions = new ArrayList<Role2Resource2Permission>();
      role.setResource2Permissions(resource2Permissions);
      if (null==resourceIds||resourceIds.length == 0) {
        resourceIds = new Long[0];
      }
      else if(resourceIds.length== 1){
        Long[][] temp = new Long[1][permissionIds.length];
        for (int i = 0; i < permissionIds.length; i++) {
          temp[0][i] = permissionIds[i][0];
        }
        permissionIds = temp;
      }
      //取对应资源，更新权限。
      for(int i=0 ;i<resourceIds.length;i++){
        
        //拼接权限描述
        Set<Long> permissionIdSet = Sets.newHashSet();
        List<String> list = new ArrayList<String>();
        for (Long permissionId : permissionIds[i]) {
            permissionIdSet.add(permissionId);
            list.add(permissionId.toString());
        }
        
        Role2Resource2Permission role2Resource2Permission = role2Resource2PermissionService.getRole2Resource2Permission(role.getId(), resourceIds[i]);
        if(null == role2Resource2Permission){
          
          role2Resource2Permission = new Role2Resource2Permission(role.getId(),resourceIds[i], StringJoinUtils.join(",", permissionIdSet));
        }
        resource2Permissions.add(role2Resource2Permission);
        //更新角色授权
        role2Resource2PermissionService.updateAuth(role2Resource2Permission, list);
      }
  }
  
  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String createWithResourcePermission(
          Model model,
          @Valid @ModelAttribute("m") Role role, BindingResult result,
          @RequestParam("resourceId") Long[] resourceIds,
          @RequestParam("permissionIds") Long[][] permissionIds,
          RedirectAttributes redirectAttributes) {
    
      
      super.create(model, role, result, redirectAttributes);
      
      fillResourcePermission(role, resourceIds, permissionIds);
      
      return redirectToUrl(null);
  }
  
  /**
   * 删除角色(连带删除角色对应的资源权限):批量操作
   * @param ids
   * @param backURL
   * @param redirectAttributes
   * @return
   */
  @RequestMapping(value = "batch/delete", method = { RequestMethod.GET, RequestMethod.POST })
  public String deleteInBatch(@RequestParam(value = "ids", required = false) Long[] ids,
      @RequestParam(value = Constants.BACK_URL, required = false) String backURL, RedirectAttributes redirectAttributes) {

    if (permissionList != null) {
      this.permissionList.assertHasDeletePermission();
    }
    //删除角色
    baseService.delete(ids);
    for(Long id : ids){
      role2Resource2PermissionService.deleteByRole(id);
    }
    
    redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
    return redirectToUrl(backURL);
  }
  
  /**
   * 删除角色:连带删除角色对应资源权限
   */
  @RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
  public String delete(@PathVariable("id") Long id, @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
      RedirectAttributes redirectAttributes) {

    if (permissionList != null) {
      this.permissionList.assertHasDeletePermission();
    }

    baseService.delete(id);
    role2Resource2PermissionService.deleteByRole(id);
    redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
    return redirectToUrl(backURL);
  }
}

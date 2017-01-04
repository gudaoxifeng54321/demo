package com.ly.sys.core.resource.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ly.sys.common.service.BaseTreeableService;
import com.ly.sys.core.auth.service.AuthService;
import com.ly.sys.core.resource.dao.ResourceDao;
import com.ly.sys.core.resource.vo.Resource;
import com.ly.sys.core.resource.vo.temp.Menu;
import com.ly.sys.core.user.vo.Operator;

/**
 * <p>Description: this is ResourceServiceImpl
 * <p>User: mtwu
 * <p>Date: 2016-6-12 16:33:31
 * <p>Version: 1.0
 */
@Service
public class ResourceService extends BaseTreeableService<Resource, Long> {
  private final static Logger logger = LoggerFactory.getLogger(ResourceService.class);
  @Autowired
  private AuthService authService;

  private ResourceDao getResourceDao() {
    return (ResourceDao) baseDao;
  }

  public List<Resource> findListByQuery(Map<String, Object> queryMap) throws SystemException {
    List<Resource> list = getResourceDao().findListByQuery(queryMap);
    return list;
  }

  /**
   * 复制属性
   * 
   * @param adsiteorder
   * @param adsiteorderForm
   */
  public void copyProperties(Resource dest, Resource orig) {
    if (orig.getName() != null)
      dest.setName(orig.getName());
    if (orig.getSource() != null)
      dest.setSource(orig.getSource());
    if (orig.getIdentity() != null)
      dest.setIdentity(orig.getIdentity());
    if (orig.getUrl() != null)
      dest.setUrl(orig.getUrl());
    if (orig.getParentId() != null)
      dest.setParentId(orig.getParentId());
    if (orig.getParentIds() != null)
      dest.setParentIds(orig.getParentIds());
    if (orig.getIcon() != null)
      dest.setIcon(orig.getIcon());
    if (orig.getWeight() != null)
      dest.setWeight(orig.getWeight());
    if (orig.getIsParent() != null)
      dest.setIsParent(orig.getIsParent());
    if (orig.getIsShow() != null)
      dest.setIsShow(orig.getIsShow());
    if (orig.getDeleted() != null)
      dest.setDeleted(orig.getDeleted());
  }

  /**
   * 得到真实的资源标识 即 父亲:儿子
   * 
   * @param resource
   * @return
   */
  public String findActualResourceIdentity(Resource resource) {

    if (resource == null) {
      return null;
    }

    StringBuilder s = new StringBuilder(resource.getIdentity() == null ? "" : resource.getIdentity());

    boolean hasResourceIdentity = !StringUtils.isEmpty(resource.getIdentity());

    Resource parent = findById(resource.getParentId());
    while (parent != null) {
      if (!StringUtils.isEmpty(parent.getIdentity())) {
        s.insert(0, parent.getIdentity() + ":");
        hasResourceIdentity = true;
      }
      parent = findById(parent.getParentId());
    }

    // 如果用户没有声明 资源标识 且父也没有，那么就为空
    if (!hasResourceIdentity) {
      return "";
    }

    // 如果最后一个字符是: 因为不需要，所以删除之
    int length = s.length();
    if (length > 0 && s.lastIndexOf(":") == length - 1) {
      s.deleteCharAt(length - 1);
    }

    // 如果有儿子 最后拼一个*
    boolean hasChildren = false;
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("parentId", resource.getId());
    List<Resource> list = findListByQuery(map);
    if (null != list && list.size() > 0) {
      hasChildren = Boolean.TRUE;
    }
    if (hasChildren) {
      s.append(":*");
    }

    return s.toString();
  }

  /**
   * 查询用户对应权限菜单
   * 
   * @param user
   * @return
   */
  public List<Menu> findMenus(Operator operator) {

    List<Resource> resources = getResourceDao().findList();
    Set<String> userPermissions = authService.findStringPermissions(operator);
    Iterator<Resource> iter = resources.iterator();
    while (iter.hasNext()) {
      Resource resource = iter.next();
      if (!hasPermission(resource, userPermissions)) {
        iter.remove();
      }
    }
    return convertToMenus(resources);
  }

  public Integer findCountChildrenByParentId(Long parentId) {

    return getResourceDao().findCountChildrenByParentId(parentId);
  }

  /**
   * 根据菜单标识查询菜单
   * 
   * @return
   */
  public Resource findMenuByIdentity(String identity) {
    Resource resource = null;
    Map<String, Object> queryMap = new HashMap<String, Object>();
    queryMap.put("identity", identity);
    List<Resource> resources = findListByQuery(queryMap);
    if (!resources.isEmpty()) {
      resource = resources.get(0);
    }
    return resource;
  }

  private boolean hasPermission(Resource resource, Set<String> userPermissions) {
    String actualResourceIdentity = findActualResourceIdentity(resource);
    if (StringUtils.isEmpty(actualResourceIdentity)) {
      return true;
    }

    for (String permission : userPermissions) {
      if (hasPermission(permission, actualResourceIdentity)) {
        return true;
      }
    }

    return false;
  }

  private boolean hasPermission(String permission, String actualResourceIdentity) {

    // 得到权限字符串中的 资源部分，如a:b:create --->资源是a:b
    String permissionResourceIdentity = permission.substring(0, permission.lastIndexOf(":"));

    // 如果权限字符串中的资源 是 以资源为前缀 则有权限 如a:b 具有a:b的权限
    if (permissionResourceIdentity.startsWith(actualResourceIdentity)) {
      return true;
    }

    // 模式匹配
    WildcardPermission p1 = new WildcardPermission(permissionResourceIdentity);
    WildcardPermission p2 = new WildcardPermission(actualResourceIdentity);

    return p1.implies(p2) || p2.implies(p1);
  }

  @SuppressWarnings("unchecked")
  public static List<Menu> convertToMenus(List<Resource> resources) {
    if (resources.size() == 0) {
      return Collections.EMPTY_LIST;
    }
    Menu root = convertToMenu(resources.remove(resources.size() - 1));

    recursiveMenu(root, resources);
    List<Menu> menus = new ArrayList<Menu>();
    List<Menu> _menus = root.getChildren();
    for (int i = 0; i < _menus.size(); i++) {
      menus.addAll(_menus.get(i).getChildren());
    }
    _menus = null;
    removeNoLeafMenu(menus);

    return menus;
  }

  private static void removeNoLeafMenu(List<Menu> menus) {
    if (menus.size() == 0) {
      return;
    }
    for (int i = menus.size() - 1; i >= 0; i--) {
      Menu m = menus.get(i);
      removeNoLeafMenu(m.getChildren());
      if (!m.isHasChildren() && StringUtils.isEmpty(m.getUrl())) {
        menus.remove(i);
      }
    }
  }

  private static void recursiveMenu(Menu menu, List<Resource> resources) {
    for (int i = resources.size() - 1; i >= 0; i--) {
      Resource resource = resources.get(i);
      if (resource.getParentId().equals(menu.getId())) {
        menu.getChildren().add(convertToMenu(resource));
        resources.remove(i);
      }
    }

    for (Menu subMenu : menu.getChildren()) {
      recursiveMenu(subMenu, resources);
    }
  }

  private static Menu convertToMenu(Resource resource) {
    return new Menu(resource.getId(), resource.getName(), resource.getSource(), resource.getIcon(), resource.getUrl(),
        resource.getIdentity());
  }

}

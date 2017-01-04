package com.ly.sys.core.user.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ly.sys.common.entity.search.Searchable;
import com.ly.sys.common.service.BaseService;
import com.ly.sys.common.utils.UserLogUtils;
import com.ly.sys.core.user.dao.OperatorDao;
import com.ly.sys.core.user.exception.UserBlockedException;
import com.ly.sys.core.user.exception.UserNotExistsException;
import com.ly.sys.core.user.vo.Operator;

/**
 * <p>
 * Description: this is Operator Service
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-12 16:33:31
 * <p>
 * Version: 1.0
 */
@Service
public class OperatorService extends BaseService<Operator, Long>{

  
  @Resource
  private PasswordService              passwordService;
  @Resource
  private OperatorStatusHistoryService operatorStatusHistoryService;
  @Autowired
  private OperatorDao operatorDao;
  @Autowired
  private UserService userService;
  /*@Autowired
  private ThirdOperatorService thirdOperatorService;*/
  
  
  private OperatorDao getOperatorDao() {
    return (OperatorDao) baseDao;
  }
  
  public List<Operator> getAllOperators(){
    return operatorDao.getAllOperators();
  }
  
  public Operator findSysById(Integer id, String originSource){
    return operatorDao.findSysById(id, originSource);
  }

  /**
   * 浣跨敤BeanUtils鐨勬垚鏈儕浜哄湴鏄傝吹,鎵嬪姩鍐欐槸鏈�鏁堢巼鐨勶紝缂虹偣澧炲姞浜嗕唬鐮侀暱搴﹀拰闃呰浠ｇ爜鐨勯毦搴�
   * 
   * @param adsiteorder
   * @param adsiteorderForm
   */
  public void copyProperties(Operator dest, Operator orig) {
    if (orig.getSource() != null)
      dest.setSource(orig.getSource());
    if (orig.getUsername() != null)
      dest.setUsername(orig.getUsername());
    if (orig.getEmail() != null)
      dest.setEmail(orig.getEmail());
    if (orig.getMobile() != null)
      dest.setMobile(orig.getMobile());
    if (orig.getPassword() != null)
      dest.setPassword(orig.getPassword());
    if (orig.getStatus() != null)
      dest.setStatus(orig.getStatus());
    if (orig.getIsAdmin() != null)
      dest.setIsAdmin(orig.getIsAdmin());
    if (orig.getDeleted() != null)
      dest.setDeleted(orig.getDeleted());
    if (orig.getCreateDate() != null)
      dest.setCreateDate(orig.getCreateDate());
  }

  
  public Operator findByUsername(String username) {
    if (StringUtils.isEmpty(username)) {
      return null;
    }
    return getOperatorDao().findByUsername(username);
  }

  
  public Operator findByEmail(String email) {
    if (StringUtils.isEmpty(email)) {
      return null;
    }
    return getOperatorDao().findByEmail(email);
  }

  
  public Operator findByMobilePhoneNumber(String mobilePhoneNumber) {
    if (StringUtils.isEmpty(mobilePhoneNumber)) {
      return null;
    }
    return getOperatorDao().findByMobilePhoneNumber(mobilePhoneNumber);
  }

  
  public Operator changePassword(Operator operator, String newPassword) {
    //修改go2
   /* ThirdOperator thirdOperator = thirdOperatorService.findThirdOperatorsById(operator.getId().intValue(),DataSources.GO2);
    if(thirdOperator!=null){
      thirdOperator.setPassword(userService.getMd5Password(newPassword, operator.getSource(), 1));
      thirdOperatorService.updateThirdOperatorPassword(thirdOperator, DataSources.GO2);
    }*/
    operator.setPassword(passwordService.encryptPassword(operator.getUsername(), newPassword));
    update(operator);
    return operator;
  }

  
  public Operator changeStatus(Operator opOperator, Operator operator, int newStatus, String reason) {
    operator.setStatus(newStatus);
    update(operator);
    operatorStatusHistoryService.log(opOperator, operator, newStatus, reason);
    return operator;
  }

  
  public Operator login(String username, String password) {

    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
      UserLogUtils.log(username, "loginError", "username is empty");
      throw new UserNotExistsException();
    }

    Operator operator = null;

    // 此处需要走代理对象，目的是能走缓存切面
    OperatorService proxyOperatorService = (OperatorService) AopContext.currentProxy();
    if (operator == null) {
      operator = proxyOperatorService.findByUsername(username);
    }

    if (operator == null) {
      operator = proxyOperatorService.findByEmail(username);
    }
    if (operator == null) {
      operator = proxyOperatorService.findByMobilePhoneNumber(username);
    }
    if (operator == null || Boolean.TRUE.equals(operator.getDeleted())) {
      UserLogUtils.log(username, "loginError", "user is not exists!");

      throw new UserNotExistsException();
    }

    passwordService.validate(operator, password);

    if (operator.getStatus() == 0) {
      UserLogUtils.log(username, "loginError", "user is blocked!");
      throw new UserBlockedException(username + "被封禁，不能登录！");
    }

    UserLogUtils.log(username, "loginSuccess", "");
    return operator;
  }

  
  public void changePassword(Operator opOperator, String[] ids, String newPassword) {
    OperatorService proxyUserService = (OperatorService) AopContext.currentProxy();
    for (String id : ids) {
      Operator operator = getOperatorDao().findById(Long.parseLong(id));
      proxyUserService.changePassword(operator, newPassword);
      UserLogUtils.log(operator.getUsername(), "changePassword", "admin user {} change password!", opOperator.getUsername());
    }
  }

  
  public void changeStatus(Operator opOperator, String[] ids, int newStatus, String reason) {
    OperatorService proxyUserService = (OperatorService) AopContext.currentProxy();
    for (String id : ids) {
      Operator user = getOperatorDao().findById(Long.valueOf(id));
      proxyUserService.changeStatus(opOperator, user, newStatus, reason);
      
        UserLogUtils.log( user.getUsername(), "changeStatus", "admin user {} change status!", opOperator.getUsername());
       
    }

  }
  
  public Set<Map<String, Object>> findIdAndNames(Searchable searchable, String usernme) {
  
  //  return  new HashSet<Map<String, Object>>();
//    searchable.addSearchFilter("username", SearchOperator.like, usernme);
    searchable.addSearchParam("username_like", usernme);
//    searchable.addSearchFilter("deleted", SearchOperator.eq, false);
  
    return Sets.newHashSet(
            Lists.transform(
                    findList(searchable),
                    new Function<Operator, Map<String, Object>>() {
                        @Override
                        public Map<String, Object> apply(Operator input) {
                            Map<String, Object> data = Maps.newHashMap();
                            data.put("label", input.getUsername());
                            data.put("value", input.getId());
                            return data;
                        }
                    }
            )
    );
    
//    Set<Map<String, Object>> set = new HashSet<Map<String, Object>>();
//    Map<String, Object> map = new HashMap<String, Object>();
//    map.put("1", "name");
//    set.add(map);
//    return set;
  }
  
}

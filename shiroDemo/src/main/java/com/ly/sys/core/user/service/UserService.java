package com.ly.sys.core.user.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.ly.common.X;
import com.ly.common.encrypt.MD5;
import com.ly.sys.common.service.BaseService;
import com.ly.sys.core.user.dao.EditLogDao;
import com.ly.sys.core.user.dao.UserDao;
import com.ly.sys.core.user.vo.EditLog;
import com.ly.sys.core.user.vo.Result;
import com.ly.sys.core.user.vo.User;

 /**
 * <p>Description: this is User Service
 * <p>User: mtwu
 * <p>Date: 2016-6-12 16:33:30
 * <p>Version: 1.0
 */
@Service
public class UserService extends BaseService<User, Long>{
  Logger logger = LoggerFactory.getLogger(UserService.class);
  
  
  @Resource
  private EditLogDao editLogDao;  
  /*@Resource
  private ThirdUserService thirdUserService;*/
  
//缓存，放用户中的一些数据信息
  public static Map<String,String> map = new HashMap<String, String>(3);
  
  static{
    map.put("1", "username");
    map.put("2", "email");
    map.put("3", "mobile");
  }
  
  private UserDao getUserDao(){
    
    return (UserDao)baseDao;
  }
  
  public List<User> check(String param, String number, String source) {
    return getUserDao().check(param , map.get(number) , source);
  }
  

  public List<User> findUser(String username,String password,String source){
    return getUserDao().findUser(username, password,source);
  }
  
  public List<User> findUserByIds(List<Integer> ids){
    return getUserDao().findUserByIds(ids);
  }
  
  public void insert(User user) {
    getUserDao().insert(user);
  }

  
  public User findById(Long id) {
    return getUserDao().findById(id);
  }

  
  public static void main(String[] args) {
    UserService u = new UserService();
    System.out.println(u.getMd5Password("12345", "e3e3",0));
  }

  
  public String getMd5Password(String p,String source,Integer newEncrypted) {
    try {
      if(newEncrypted!=null&&newEncrypted==1){
        source = "go2";
      }
      return MD5.md5Encode(X.getConfig("PASSWORD_SALT_db_"+source)+p);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  
  
  
  /**
   * 修改密码
   * @param username
   * @return
   */
  public Result<Object> modifyPassword(Long id,String password,String oldPassword){
    Result<Object> result = new Result<Object>();
    EditLog editLog = new EditLog();
    User u = getUserDao().findById(id);
    if(u==null){
      result.setResult(213, false, "not.exits", "该用户");
      logger.error("{}该用户不存在"+id);
      return result;
    }
    editLog.setContent(u.getPassword());
    
    //验证旧密码是否相等
    String oldPasswordMd5 = getMd5Password(oldPassword,  u.getSource(), u.getNewEncrypted());
    if(!u.getPassword().equals(oldPasswordMd5)){
      result.setResult(209, false, "not.correct", "旧密码");
      logger.error("旧密码错误"+id);
      return result;
    }
    //生成新的password
    String newPasswordMd5 = getMd5Password(password, null, 1);
    u.setPassword(newPasswordMd5);
    u.setNewEncrypted(1);
    UserService proxyUserService = (UserService) AopContext.currentProxy();
    proxyUserService.update(u);
    editLog.setUserId(id);
    editLog.setType(2); //修改密码
    editLog.setCreateDate(new Date());
    editLogDao.insert(editLog);
    result.setResult(200, true, "handle.success", "修改");
    return result;
  }

  public int updateUserMobileApprove(Long id,String mobile){
    User u = getUserDao().findById(id);
    if(u==null){
      return 0;
    }
    //执行老数据修改
    
    EditLog editLog = new EditLog();
    editLog.setUserId(id);
    editLog.setContent(u.getMobile());
    editLog.setType(2); //修改手机
    editLog.setCreateDate(new Date());
    editLogDao.insert(editLog);
    return getUserDao().updateUserMobileApprove(id, mobile);
  }
  

  public void batchInsert(List<User> list){
    getUserDao().batchInsert(list);
  }
  
  public void batchUpdate(List<User> list){
    getUserDao().batchUpdate(list);
  }

  /**
   * 根据用户名密码去匹配四个平台的用户
   * @param username
   * @param password
   * @return
   */
  public List<User> userLists4Platforms(String username,String password,List<User> users){
    List<User> resultUsers = new ArrayList<>();
    //List<User> users = check(username, "1", null);
    for (int i = 0; i < users.size(); i++) {
      User u = users.get(i);
      Integer newEncrypted = u.getNewEncrypted();
      String source = u.getSource();
      try {
        if(getMd5Password(password, source, newEncrypted).equals(u.getPassword())){
          resultUsers.add(u);
        }
      } catch (Exception e) {
        logger.error(null,e);
      }
    }
    return resultUsers;
  }
  
  public User findByThirdIdAndSource(Long thirdId,String source){
    return getUserDao().findByThirdIdAndSource(thirdId,source);
  }
  

  @Override
  public void copyProperties(User dest, User orig) {
    // TODO Auto-generated method stub
    
  }
}



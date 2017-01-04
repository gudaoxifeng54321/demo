package com.ly.sys.core.user.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.common.mvc.Pagination;
import com.ly.sys.common.exception.SystemException;
import com.ly.sys.common.service.BaseService;
import com.ly.sys.core.user.dao.UserSecurityDao;
import com.ly.sys.core.user.vo.UserSecurity;

 /**
 * <p>Description: this is UserSecurity Service
 * <p>User: mtwu
 * <p>Date: 2016-6-12 16:33:31
 * <p>Version: 1.0
 */
@Service
public class UserSecurityService extends BaseService<UserSecurity, Long>{

  Logger logger = LoggerFactory.getLogger(UserSecurityService.class);
  
  
  private UserSecurityDao getUserSecurityDao(){
    
    return (UserSecurityDao)baseDao;
  }
  
  public PageInfo<UserSecurity> findPageList(Map<String,Object> queryMap,Pagination pagination) throws SystemException {
    PageHelper.startPage(pagination.getPageNum(), pagination.getPageSize());
    List<UserSecurity> list =getUserSecurityDao().findPageList(queryMap);
    PageInfo<UserSecurity> pi = new PageInfo<UserSecurity>(list, pagination.getNavigationSize());
    return pi;
  }
    /*
    public List<UserSecurity> findList() throws SystemException {
      List<UserSecurity> list =getUserSecurityDao().findList();
    return list;
  }
    */
    public UserSecurity findById(Long id)  throws SystemException{
      UserSecurity usersecurity=getUserSecurityDao().findById(id);
    return usersecurity;
  }
  
  
  public boolean save(UserSecurity usersecurityForm) throws SystemException {
    UserSecurity usersecurity = null;
      if ( usersecurityForm.getId() == null ) {
        usersecurity = usersecurityForm;
      } else {
        usersecurity =findById(usersecurityForm.getId());
      }
      if(usersecurity!=null){
      copyProperties(usersecurity, usersecurityForm);
      
      if ( usersecurity.getId() != null && usersecurity.getId() > 0 ) {
        update(usersecurity);
      } else {
        add(usersecurity);
      }
      usersecurityForm = usersecurity;
      return true;
    }
    return false;
  }
  
    public void delete(Long[] ids) throws SystemException {
      Integer len = ids.length;
      for ( int i = 0; i < len; i++ ) {
        Long id=ids[i];
        if(null != id){
          delete(id);
        }
      }
  }
    public void delete(Long id) throws SystemException {
      getUserSecurityDao().delete(id);
  }
  
  /**
   * 浣跨敤BeanUtils鐨勬垚鏈儕浜哄湴鏄傝吹,鎵嬪姩鍐欐槸鏈�鏁堢巼鐨勶紝缂虹偣澧炲姞浜嗕唬鐮侀暱搴﹀拰闃呰浠ｇ爜鐨勯毦搴�
   * @param adsiteorder
   * @param adsiteorderForm
   */
   public void copyProperties(UserSecurity dest,UserSecurity orig){
                          if(orig.getUserId() != null) dest.setUserId(orig.getUserId());
                        if(orig.getAQuestion() != null) dest.setAQuestion(orig.getAQuestion());
                        if(orig.getAAnswer() != null) dest.setAAnswer(orig.getAAnswer());
                        if(orig.getBQuestion() != null) dest.setBQuestion(orig.getBQuestion());
                        if(orig.getBAnswer() != null) dest.setBAnswer(orig.getBAnswer());
                        if(orig.getCQuestion() != null) dest.setCQuestion(orig.getCQuestion());
                        if(orig.getCAnswer() != null) dest.setCAnswer(orig.getCAnswer());
                        if(orig.getSafeEmail() != null) dest.setSafeEmail(orig.getSafeEmail());
               }
 
  public UserSecurity findByUserId(Long userId) {
    return getUserSecurityDao().findByUserId(userId);
  }
  
  public boolean updateOrAdd(UserSecurity userSecurity) {
    try {
    //先查询是否存在
      UserSecurity u = getUserSecurityDao().findByUserId(userSecurity.getUserId());
      if(u==null){
        //执行插入
        getUserSecurityDao().insert(userSecurity);
        return true;
      }else{
        //执行update
        copyProperties(userSecurity, u);
        getUserSecurityDao().update(u);
        return true;
      }
    } catch (Exception e) {
      logger.info(null,e);
      return false;
    }
    
  }
}



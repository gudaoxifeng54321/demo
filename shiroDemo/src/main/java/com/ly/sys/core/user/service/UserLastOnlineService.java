package com.ly.sys.core.user.service;

import org.springframework.stereotype.Service;

import com.ly.sys.common.service.BaseService;
import com.ly.sys.core.user.dao.UserLastOnlineDao;
import com.ly.sys.core.user.vo.UserLastOnline;

 /**
 * <p>Description: this is UserLastOnline Service
 * <p>User: mtwu
 * <p>Date: 2016-6-12 16:33:31
 * <p>Version: 1.0
 */
@Service
public class UserLastOnlineService extends BaseService<UserLastOnline, Long>{

  
  public UserLastOnlineDao getUserLastOnlineDao(){
    
    return (UserLastOnlineDao)baseDao;
  }
 
  /**
   * 浣跨敤BeanUtils鐨勬垚鏈儕浜哄湴鏄傝吹,鎵嬪姩鍐欐槸鏈�鏁堢巼鐨勶紝缂虹偣澧炲姞浜嗕唬鐮侀暱搴﹀拰闃呰浠ｇ爜鐨勯毦搴�
   * @param adsiteorder
   * @param adsiteorderForm
   */
   public void copyProperties(UserLastOnline dest,UserLastOnline orig){
                          if(orig.getUserId() != null) dest.setUserId(orig.getUserId());
                        if(orig.getUsername() != null) dest.setUsername(orig.getUsername());
                        if(orig.getLastLoginTimestamp() != null) dest.setLastLoginTimestamp(orig.getLastLoginTimestamp());
                        if(orig.getLoginCount() != null) dest.setLoginCount(orig.getLoginCount());
                        if(orig.getHost() != null) dest.setHost(orig.getHost());
                        if(orig.getUserAgent() != null) dest.setUserAgent(orig.getUserAgent());
               }
  
  public UserLastOnline findByUserId(Long userId) {
    return getUserLastOnlineDao().findByUserId(userId);
  }
  
  
  public void updateOrAdd(UserLastOnline userLastOnline) {
      UserLastOnline last = getUserLastOnlineDao().findByUserId(userLastOnline.getUserId());
      if(last != null){
        userLastOnline.setLoginCount(last.getLoginCount() + userLastOnline.getLoginCount());
        userLastOnline.setId(last.getId());
        this.update(userLastOnline);
      }else{
        this.add(userLastOnline);
      }
  }

}



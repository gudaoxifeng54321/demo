package com.ly.sys.core.user.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.common.X;
import com.ly.common.mvc.Pagination;
import com.ly.sys.common.service.BaseService;
import com.ly.sys.core.user.dao.OperatorOnlineDao;
import com.ly.sys.core.user.vo.OperatorLastOnline;
import com.ly.sys.core.user.vo.OperatorOnline;

/**
 * <p>Description: this is OperatorOnline Service
 * <p>User: mtwu
 * <p>Date: 2016-6-15 15:41:11
 * <p>Version: 1.0
 */
@Service
public class OperatorOnlineService extends BaseService<OperatorOnline, String> {

  @Autowired
  private OperatorLastOnlineService operatorLastOnlineService;

  public OperatorOnlineDao getOperatorOnlineDao(){
    return (OperatorOnlineDao)baseDao;
  }

  /**
   * 浣跨敤BeanUtils鐨勬垚鏈儕浜哄湴鏄傝吹,鎵嬪姩鍐欐槸鏈�晥鐜囩殑锛岀己鐐瑰鍔犱簡浠ｇ爜闀垮害鍜岄槄璇讳唬鐮佺殑闅惧害
   * 
   * @param adsiteorder
   * @param adsiteorderForm
   */
  public void copyProperties(OperatorOnline dest, OperatorOnline orig) {
    if (orig.getOperatorId() != null)
      dest.setOperatorId(orig.getOperatorId());
    if (orig.getUsername() != null)
      dest.setUsername(orig.getUsername());
    if (orig.getHost() != null)
      dest.setHost(orig.getHost());
    if (orig.getSystemHost() != null)
      dest.setSystemHost(orig.getSystemHost());
    if (orig.getUserAgent() != null)
      dest.setUserAgent(orig.getUserAgent());
    if (orig.getStatus() != null)
      dest.setStatus(orig.getStatus());
    if (orig.getStartTimestamp() != null)
      dest.setStartTimestamp(orig.getStartTimestamp());
    if (orig.getLastAccessTime() != null)
      dest.setLastAccessTime(orig.getLastAccessTime());
    if (orig.getTimeout() != null)
      dest.setTimeout(orig.getTimeout());
    if (orig.getSession() != null)
      dest.setSession(orig.getSession());
  }

  public void deleteTimeout() {
    String offTime = X.getConfig("user.off.line.time.minute");
    getOperatorOnlineDao().deleteTimeout(Integer.parseInt(offTime));
  }

  public void online(OperatorOnline operatorOnline) {
    save(operatorOnline);

  }

  public void offline(String sid) {
    OperatorOnline operatorOnline = getOperatorOnlineDao().findById(sid);
    if (operatorOnline != null) {
      delete(sid);
    }
    if (operatorOnline.getOperatorId() != null) {
      operatorLastOnlineService.lastOnline(OperatorLastOnline.fromOperatorOnline(operatorOnline));
    }

  }

  public void batchOffline(List<String> needOfflineIdList) {
    String[] ids = new String[needOfflineIdList.size()];
    for (int i = 0; i < needOfflineIdList.size(); i++) {
      ids[i] = needOfflineIdList.get(i);
      offline(ids[i]);
    }
    //delete(ids);

  }

  public PageInfo<OperatorOnline> findExpiredUserOnlineList(Date expiredDate, Pagination pagination) {
    PageHelper.startPage(pagination.getPageNum(), pagination.getPageSize());
    List<OperatorOnline> operatorOnlines = getOperatorOnlineDao().findExpiredUserOnlineList(expiredDate);
    PageInfo<OperatorOnline> pi = new PageInfo<>(operatorOnlines, pagination.getNavigationSize());
    return pi;
  }


}

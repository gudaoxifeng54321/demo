package com.ly.sys.core.user.service;

import java.util.List;
import java.util.Map;

import org.omg.CORBA.SystemException;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.common.mvc.Pagination;
import com.ly.sys.common.service.BaseService;
import com.ly.sys.core.user.dao.OperatorLastOnlineDao;
import com.ly.sys.core.user.vo.OperatorLastOnline;


/**
 * <p>
 * Description: this is OperatorLastOnline Service
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-15 15:07:01
 * <p>
 * Version: 1.0
 */
@Service
public class OperatorLastOnlineService extends BaseService<OperatorLastOnline, Long>{


  
  public OperatorLastOnlineDao getOperatorLastOnlineDao(){
    return (OperatorLastOnlineDao) baseDao;
  }

  public PageInfo<OperatorLastOnline> findPageList(Map<String, Object> queryMap, Pagination pagination) throws SystemException {
    PageHelper.startPage(pagination.getPageNum(), pagination.getPageSize());
    List<OperatorLastOnline> list = getOperatorLastOnlineDao().findPageList(queryMap);
    PageInfo<OperatorLastOnline> pi = new PageInfo<OperatorLastOnline>(list, pagination.getNavigationSize());
    return pi;
  }
  /*
   * public List<OperatorLastOnline> findList() throws SystemException { List<OperatorLastOnline> list =getOperatorLastOnlineDao().findList();
   * return list; }
   */
  public OperatorLastOnline findById(Long id) throws SystemException {
    OperatorLastOnline operatorlastonline = getOperatorLastOnlineDao().findById(id);
    return operatorlastonline;
  }

  public boolean save(OperatorLastOnline operatorlastonlineForm) throws SystemException {
    OperatorLastOnline operatorlastonline = null;
    if (operatorlastonlineForm.getId() == null) {
      operatorlastonline = operatorlastonlineForm;
    } else {
      operatorlastonline = findById(operatorlastonlineForm.getId());
    }
    if (operatorlastonline != null) {
      copyProperties(operatorlastonline, operatorlastonlineForm);

      if (operatorlastonline.getId() != null && operatorlastonline.getId() > 0) {
        update(operatorlastonline);
      } else {
        add(operatorlastonline);
      }
      operatorlastonlineForm = operatorlastonline;
      return true;
    }
    return false;
  }

  public void delete(Long[] ids) throws SystemException {
    Integer len = ids.length;
    for (int i = 0; i < len; i++) {
      Long id = ids[i];
      if (null != id) {
        delete(id);
      }
    }
  }

  public void delete(Long id) throws SystemException {
    getOperatorLastOnlineDao().delete(id);
  }

  /**
   * 浣跨敤BeanUtils鐨勬垚鏈儕浜哄湴鏄傝吹,鎵嬪姩鍐欐槸鏈�晥鐜囩殑锛岀己鐐瑰鍔犱簡浠ｇ爜闀垮害鍜岄槄璇讳唬鐮佺殑闅惧害
   * 
   * @param adsiteorder
   * @param adsiteorderForm
   */
  public void copyProperties(OperatorLastOnline dest, OperatorLastOnline orig) {
    if (orig.getOperatorId() != null)
      dest.setOperatorId(orig.getOperatorId());
    if (orig.getUsername() != null)
      dest.setUsername(orig.getUsername());
    if (orig.getHost() != null)
      dest.setHost(orig.getHost());
    if (orig.getUserAgent() != null)
      dest.setUserAgent(orig.getUserAgent());
    if (orig.getSystemHost() != null)
      dest.setSystemHost(orig.getSystemHost());
    if (orig.getLastLoginTimestamp() != null)
      dest.setLastLoginTimestamp(orig.getLastLoginTimestamp());
    if (orig.getLastStopTimestamp() != null)
      dest.setLastStopTimestamp(orig.getLastStopTimestamp());
    if (orig.getLoginCount() != null)
      dest.setLoginCount(orig.getLoginCount());
    if (orig.getTotalOnlineTime() != null)
      dest.setTotalOnlineTime(orig.getTotalOnlineTime());
  }

  
  
  public OperatorLastOnline findByOperatorId(Long operatorId) {
    return getOperatorLastOnlineDao().findByOperatorId(operatorId);
  }

  
  public void lastOnline(OperatorLastOnline lastOnline) {
    OperatorLastOnline operatorLastOnline = findByOperatorId(lastOnline.getOperatorId());
    if (operatorLastOnline == null) {
      operatorLastOnline = lastOnline;
      operatorLastOnline.incLoginCount();
      operatorLastOnline.incTotalOnlineTime();
      add(lastOnline);
    } else {
      OperatorLastOnline.merge(lastOnline, operatorLastOnline);
      operatorLastOnline.incLoginCount();
      operatorLastOnline.incTotalOnlineTime();
      update(operatorLastOnline);
    }
  }
  
  
}

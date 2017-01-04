package com.ly.sys.core.user.service;

import java.util.Date;

import org.omg.CORBA.SystemException;
import org.springframework.stereotype.Service;

import com.ly.sys.common.service.BaseService;
import com.ly.sys.core.user.dao.OperatorStatusHistoryDao;
import com.ly.sys.core.user.vo.Operator;
import com.ly.sys.core.user.vo.OperatorStatusHistory;

/**
 * <p>
 * Description: this is OperatorStatusHistory Service
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-15 15:07:01
 * <p>
 * Version: 1.0
 */
@Service
public class OperatorStatusHistoryService extends BaseService<OperatorStatusHistory, Long>{

  
  
  
  private OperatorStatusHistoryDao getOperatorStatusHistoryDao() {
    return (OperatorStatusHistoryDao) baseDao;
  }
  

//  public PageInfo<OperatorStatusHistory> findPageList(Map<String, Object> queryMap, Pagination pagination) throws SystemException {
//    PageHelper.startPage(pagination.getPageNum(), pagination.getPageSize());
//    List<OperatorStatusHistory> list = getOperatorStatusHistoryDao().findPageList(queryMap);
//    PageInfo<OperatorStatusHistory> pi = new PageInfo<OperatorStatusHistory>(list, pagination.getNavigationSize());
//    return pi;
//  }
  

  
//  public List<OperatorStatusHistory> findList(Searchable searchable) {
//    // TODO Auto-generated method stub
//    return null;
//  }
  
  /*
   * public List<OperatorStatusHistory> findList() throws SystemException { List<OperatorStatusHistory> list
   * =getOperatorStatusHistoryDao().findList(); return list; }
   */
  public OperatorStatusHistory findById(Long id) throws SystemException {
    OperatorStatusHistory operatorstatushistory = getOperatorStatusHistoryDao().findById(id);
    return operatorstatushistory;
  }


  public boolean save(OperatorStatusHistory operatorstatushistoryForm) throws SystemException {
    OperatorStatusHistory operatorstatushistory = null;
    if (operatorstatushistoryForm.getId() == null) {
      operatorstatushistory = operatorstatushistoryForm;
    } else {
      operatorstatushistory = findById(operatorstatushistoryForm.getId());
    }
    if (operatorstatushistory != null) {
      copyProperties(operatorstatushistory, operatorstatushistoryForm);

      if (operatorstatushistory.getId() != null && operatorstatushistory.getId() > 0) {
        update(operatorstatushistory);
      } else {
        add(operatorstatushistory);
      }
      operatorstatushistoryForm = operatorstatushistory;
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
    getOperatorStatusHistoryDao().delete(id);
  }

  /**
   * 浣跨敤BeanUtils鐨勬垚鏈儕浜哄湴鏄傝吹,鎵嬪姩鍐欐槸鏈�晥鐜囩殑锛岀己鐐瑰鍔犱簡浠ｇ爜闀垮害鍜岄槄璇讳唬鐮佺殑闅惧害
   * 
   * @param adsiteorder
   * @param adsiteorderForm
   */
  public void copyProperties(OperatorStatusHistory dest, OperatorStatusHistory orig) {
    if (orig.getOperatorId() != null)
      dest.setOperatorId(orig.getOperatorId());
    if (orig.getStatus() != null)
      dest.setStatus(orig.getStatus());
    if (orig.getReason() != null)
      dest.setReason(orig.getReason());
    if (orig.getOpOperatorId() != null)
      dest.setOpOperatorId(orig.getOpOperatorId());
    if (orig.getOpDate() != null)
      dest.setOpDate(orig.getOpDate());
  }

  
  public void log(Operator opOperator, Operator operator, int newStatus, String reason) {
    OperatorStatusHistory history = new OperatorStatusHistory();
    history.setOperatorId(operator.getId());
    history.setOpOperatorId(opOperator.getId());
    history.setOpDate(new Date());
    history.setStatus(newStatus);
    history.setReason(reason);
    save(history);

  }


  public OperatorStatusHistory findLastHistory(Operator operator) {
    return getOperatorStatusHistoryDao().findLastHistory(operator);
  }

 
  public String getLastReason(Operator operator) {
    OperatorStatusHistory history = findLastHistory(operator);
    if (history == null) {
      return "";
    }
    return history.getReason();
  }
}

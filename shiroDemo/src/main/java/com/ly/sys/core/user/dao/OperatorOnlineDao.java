package com.ly.sys.core.user.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ly.sys.common.dao.BaseDao;
import com.ly.sys.core.user.vo.OperatorOnline;

/**
 * <p>
 * Description: this is OperatorOnlineDao
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-15 15:41:11
 * <p>
 * Version: 1.0
 */
@Repository
public interface OperatorOnlineDao extends BaseDao<OperatorOnline,String> {

  

  /**
   * 操作员自动下线时间
   * 
   * @param offTime
   */
  public void deleteTimeout(@Param("offTime") int offTime);

  /**
   * 获得过期的用户
   * 
   * @param expiredDate
   * @return
   */
  public List<OperatorOnline> findExpiredUserOnlineList(@Param("expiredDate")Date expiredDate);
}

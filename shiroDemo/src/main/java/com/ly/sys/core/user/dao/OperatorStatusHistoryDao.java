package com.ly.sys.core.user.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ly.sys.common.dao.BaseDao;
import com.ly.sys.core.user.vo.Operator;
import com.ly.sys.core.user.vo.OperatorStatusHistory;

/**
 * <p>
 * Description: this is OperatorStatusHistoryDao
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-15 15:07:01
 * <p>
 * Version: 1.0
 */
@Repository
public interface OperatorStatusHistoryDao extends BaseDao<OperatorStatusHistory, Long> {
  public OperatorStatusHistory findLastHistory(@Param("operator") Operator operator);
}

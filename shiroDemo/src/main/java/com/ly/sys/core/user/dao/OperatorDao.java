package com.ly.sys.core.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ly.sys.common.dao.BaseDao;
import com.ly.sys.core.user.vo.Operator;

/**
 * <p>
 * Description: this is OperatorDao
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-12 16:33:31
 * <p>
 * Version: 1.0
 */
@Repository
public interface OperatorDao extends BaseDao<Operator, Long> {

  /**
   * 根据用户名找用户
   * 
   * @param username
   * @return
   */
  public Operator findByUsername(@Param("username") String username);

  /* *
   * /** 根据邮箱找操作员
   * 
   * @param email
   * 
   * @return
   */
  public Operator findByEmail(@Param("email") String email);

  /**
   * /** 根据手机找操作员
   * 
   * @param mobilePhoneNumber
   * 
   * @return
   */
  public Operator findByMobilePhoneNumber(@Param("mobilePhoneNumber") String mobilePhoneNumber);
  
  
  
  public Operator findSysById(@Param("id") Integer id, @Param("source") String originSource);
  
  
  public List<Operator> getAllOperators();
  
}
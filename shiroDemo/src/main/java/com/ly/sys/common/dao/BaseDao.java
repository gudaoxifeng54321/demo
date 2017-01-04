package com.ly.sys.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ly.common.mvc.DAO;
import com.ly.sys.common.entity.AbstractEntity;

public interface BaseDao<M extends AbstractEntity, ID extends Serializable> extends DAO{
  /**
   * page query Permission information
   * @param queryMap
   * @return ArrayList
   */
    public List<M> findPageList(Map<String,Object> queryMap);
    
    /**
   * query Permission information
   * @param queryPermission
   * @return ArrayList
   */
    public List<M> findList();
    
    /**
   * query Permission information Count
   * @param queryPermission
   * @return Integer
   */
    public Integer findCount(Map<String,Object> queryMap);
    
    /**
   * get one Permission information
   * @param id
   * @return Permission
   */
    public M findById(ID id);
  
  /**
   * insert one Permission information
   * @param id
   */
  public void insert(M m);
  
  /**
   * update one Permission information
   * @param Permission
   */
    public void update(M m);
    
    /**
   * delete one Permission information
   * @param id
   */
    public void delete(ID id);
}

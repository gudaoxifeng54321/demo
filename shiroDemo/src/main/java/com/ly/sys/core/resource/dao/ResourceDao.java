package com.ly.sys.core.resource.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ly.sys.common.dao.BaseTreeDao;
import com.ly.sys.core.resource.vo.Resource;

/**
 * <p>
 * Description: this is ResourceDao
 * <p>
 * User: mtwu
 * <p>
 * Date: 2016-6-12 16:33:31
 * <p>
 * Version: 1.0
 */
@Repository
public interface ResourceDao extends BaseTreeDao<Resource, Long> {

  /**
   * query Resource information
   * 
   * @param queryResource
   * @return ArrayList
   */
  public List<Resource> findListByQuery(Map<String, Object> queryMap);

  /**
   * 查询节点是否存在子节点
   * 
   * @param parentId
   * @return
   */
  public Integer findCountChildrenByParentId(Long parentId);
  
}

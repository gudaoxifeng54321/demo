package com.ly.sys.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ly.sys.common.entity.AbstractEntity;

public interface BaseTreeDao<M extends AbstractEntity<ID>, ID extends Serializable> extends BaseDao<M, ID> {

  // 删除树的所有子节点
  public void deleteChild(List<M> subNodes);

  // 下一个排序权重
  public Integer getNextWeight(ID id);

  /**
   * 查找目标节点及之后的兄弟 注意：值与越小 越排在前边
   * 
   * @param map
   * 
   * @param parentIds
   * @param currentWeight
   * @return
   */
  public List<M> findSelfAndNextSiblings(Map<String, Object> map);

  /**
   * 批量移动子节点
   * 
   * @param map
   */

  public void batchUpdate(@Param("newSourceChildrenParentIds") String newSourceChildrenParentIds,
      @Param("oldSourceChildrenParentIds") String oldSourceChildrenParentIds);

  /**
   * 查找目录数,带排序
   * @param searchable
   * @return
//   */
//  public List<M> findTreeListWithSort(Map<String, Object> map);
}

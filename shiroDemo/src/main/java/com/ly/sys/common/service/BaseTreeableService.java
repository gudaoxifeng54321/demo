package com.ly.sys.common.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ly.sys.common.dao.BaseTreeDao;
import com.ly.sys.common.entity.AbstractEntity;
import com.ly.sys.common.entity.search.Searchable;
import com.ly.sys.common.plugin.entity.Treeable;

/**
 * <p>
 * User: mtwu
 * <p>
 * Date: 13-2-22 下午5:26
 * <p>
 * Version: 1.0
 */
public abstract class BaseTreeableService<M extends AbstractEntity<ID> & Treeable<ID>, ID extends Serializable> extends BaseService<M, ID> {
  private BaseTreeDao<M, ID> getBaseTreeDao() {
    return (BaseTreeDao<M, ID>) baseDao;
  }

  @Override
  public boolean save(M m) {
    if (m.getWeight() == null) {
      m.setWeight(nextWeight(m.getParentId()));
    }
    return super.save(m);
  }

  @Transactional
  public void deleteSelfAndChild(M m) {
    if (m == null) {
      return;
    }
    // 检查它的父节点
    modifyParentFlagBeforeDelete(m.getParentId());
    // 删除该节点
    getBaseTreeDao().delete(m.getId());
    // 删除它的子及其后续所有节点

    List<M> subNodes = findChildNode(m.getId());
    if (subNodes != null && subNodes.size() > 0) {
      getBaseTreeDao().deleteChild(subNodes);
    }

  }

  /**
   * 查询父节点所有的子节点
   * 
   * @param parentId
   */
  public List<M> findChildNode(ID parentId) {
    Map<String, Object> queryMap = new HashMap<>();
    queryMap.put("parentId", parentId);
    return baseDao.findPageList(queryMap);
  }

  public void deleteSelfAndChild(List<M> mList) {
    for (M m : mList) {
      deleteSelfAndChild(m);
    }
  }

  @Transactional
  public void appendChild(M parent, M child) {
    child.setParentId(parent.getId());
    child.setParentIds(parent.makeSelfAsNewParentIds());
    child.setWeight(nextWeight(parent.getId()));
    // 修改父节点标识
    checkParentFlagBeforeAppend(parent.getId());
    save(child);
  }

  public int nextWeight(ID id) {
    return getBaseTreeDao().getNextWeight(id);
  }

  /**
   * 移动节点 根节点不能移动
   * 
   * @param source
   *          源节点
   * @param target
   *          目标节点
   * @param moveType
   *          位置
   */
  public void move(M source, M target, String moveType) {
    if (source == null || target == null || source.isRoot()) { // 根节点不能移动
      return;
    }

    // 如果是相邻的兄弟 直接交换weight即可
    boolean isSibling = source.getParentId().equals(target.getParentId());
    boolean isNextOrPrevMoveType = "next".equals(moveType) || "prev".equals(moveType);
    if (isSibling && isNextOrPrevMoveType && Math.abs(source.getWeight() - target.getWeight()) == 1) {

      // 无需移动
      if ("next".equals(moveType) && source.getWeight() > target.getWeight()) {
        return;
      }
      if ("prev".equals(moveType) && source.getWeight() < target.getWeight()) {
        return;
      }

      int sourceWeight = source.getWeight();
      source.setWeight(target.getWeight());
      target.setWeight(sourceWeight);
      // 更新数据库
      update(target);
      update(source);
      return;
    }

    // 移动到目标节点之后
    if ("next".equals(moveType)) {
      List<M> siblings = findSelfAndNextSiblings(target.getParentIds(), target.getWeight());
      siblings.remove(0);// 把自己移除

      if (siblings.size() == 0) { // 如果没有兄弟了 则直接把源的设置为目标即可
        int nextWeight = nextWeight(target.getParentId());
        updateSelftAndChild(source, target.getParentId(), target.getParentIds(), nextWeight);
        return;
      } else {
        moveType = "prev";
        target = siblings.get(0); // 否则，相当于插入到实际目标节点下一个节点之前
      }
    }

    // 移动到目标节点之前
    if ("prev".equals(moveType)) {

      List<M> siblings = findSelfAndNextSiblings(target.getParentIds(), target.getWeight());
      // 兄弟节点中包含源节点
      if (siblings.contains(source)) {
        // 1 2 [3 source] 4
        siblings = siblings.subList(0, siblings.indexOf(source) + 1);
        int firstWeight = siblings.get(0).getWeight();
        for (int i = 0; i < siblings.size() - 1; i++) {
          siblings.get(i).setWeight(siblings.get(i + 1).getWeight());
        }
        siblings.get(siblings.size() - 1).setWeight(firstWeight);
        // 保存兄弟节点的权重值到数据库
        for (M m : siblings) {
          update(m);
        }
      } else {
        // 1 2 3 4 [5 new]
        int nextWeight = nextWeight(target.getParentId());
        int firstWeight = siblings.get(0).getWeight();
        for (int i = 0; i < siblings.size() - 1; i++) {
          siblings.get(i).setWeight(siblings.get(i + 1).getWeight());
        }
        siblings.get(siblings.size() - 1).setWeight(nextWeight);
        // 保存兄弟节点的权重值到数据库
        for (M m : siblings) {
          update(m);
        }
        source.setWeight(firstWeight);
        updateSelftAndChild(source, target.getParentId(), target.getParentIds(), source.getWeight());
      }

      return;
    }
    // 否则作为最后孩子节点
    int nextWeight = nextWeight(target.getId());
    updateSelftAndChild(source, target.getId(), target.makeSelfAsNewParentIds(), nextWeight);
  }

  /**
   * 把源节点全部变更为目标节点
   * 
   * @param source
   * @param newParentIds
   */
  private void updateSelftAndChild(M source, ID newParentId, String newParentIds, int newWeight) {
    // 检查源节点的父节点
    modifyParentFlagBeforeDelete(source.getParentId());
    // 检查目标节点的父节点
    checkParentFlagBeforeAppend(newParentId);

    // 修改源节点
    String oldSourceChildrenParentIds = source.makeSelfAsNewParentIds();
    source.setParentId(newParentId);
    source.setParentIds(newParentIds);
    source.setWeight(newWeight);
    update(source);
    // 修改源节点的parent_ids
    String newSourceChildrenParentIds = source.makeSelfAsNewParentIds();
    getBaseTreeDao().batchUpdate(newSourceChildrenParentIds, oldSourceChildrenParentIds);
  }

  /**
   * 新增一个节点前，检查父节点标记
   * 
   * @param parentId
   */
  private void checkParentFlagBeforeAppend(ID parentId) {
    M parentM = baseDao.findById(parentId);
    if (parentM.getIsParent().equals("0")) {
      parentM.setIsParent("1");
      update(parentM);
    }
  }

  /**
   * 删除子节点前检查其父节点
   * 
   * @param parentId
   */
  private void modifyParentFlagBeforeDelete(ID parentId) {
    M parentM = baseDao.findById(parentId);
    if (parentM == null) {
      return;
    }
    List<M> allChilds = findChildNode(parentM.getId());
    // 源父节点，除了移动的节点外，没有其他节点了，则修改标记
    if (allChilds.size() == 1) {
      parentM.setIsParent("0");
      update(parentM);
    }
  }

  /**
   * 查找目标节点及之后的兄弟 注意：值与越小 越排在前边
   * 
   * @param parentIds
   * @param currentWeight
   * @return
   */
  protected List<M> findSelfAndNextSiblings(String parentIds, int currentWeight) {
    Map<String, Object> map = new HashMap<>();
    map.put("parentIds", parentIds);
    map.put("weight", currentWeight);
    return getBaseTreeDao().findSelfAndNextSiblings(map);
  }

  /**
   * 查看与name模糊匹配的名称
   * 
   * @param name
   * @return
   */
  public Set<String> findNames(Searchable searchable, String name, ID excludeId) {
    List<M> mList = findList(searchable);
    List<M> resList = new ArrayList<>();
    // 匹配不到的，删除
    for (M m : mList) {
      if (m.getName().toLowerCase().contains(name.toLowerCase())) {
        resList.add(m);
      }
    }
    if (excludeId != null) {

      M excludeM = findById(excludeId);
      resList = removeExcludeSearch(resList, excludeM);
    }

    return Sets.newHashSet(Lists.transform(resList, new Function<M, String>() {
      @Override
      public String apply(M input) {
        return input.getName();
      }
    }));

  }

  /**
   * 查询子子孙孙
   * 
   * @return
   */
  public List<M> findChildren(List<M> parents, Searchable searchable) {

    if (parents.isEmpty()) {
      return Collections.EMPTY_LIST;
    }
    searchable.addSearchParam("parentList", parents);
    List<M> children = findAllWithSort(searchable);
    return children;
  }

  public List<M> findAllByName(Searchable searchable, M excludeM) {
    List<M> mList = findAllWithSort(searchable);
    if (excludeM != null) {
      return removeExcludeSearch(mList, excludeM);
    } else {
      return mList;
    }

  }

  /**
   * 查找根和一级节点
   * 
   * @param searchable
   * @return
   */
  public List<M> findRootAndChild(Searchable searchable) {
    searchable.addSearchParam("parentId", 0);
    List<M> models = findAllWithSort(searchable);

    if (models.size() == 0) {
      return models;
    }
    List<ID> ids = Lists.newArrayList();
    for (int i = 0; i < models.size(); i++) {
      ids.add(models.get(i).getId());
    }
    searchable.removeSearchFilter("parentId");
    searchable.addSearchParam("parentIds", ids);

    models.addAll(findAllWithSort(searchable));

    return models;
  }

  public Set<ID> findAncestorIds(Iterable<ID> currentIds) {
    Set<ID> parents = Sets.newHashSet();
    for (ID currentId : currentIds) {
      parents.addAll(findAncestorIds(currentId));
    }
    return parents;
  }

  public Set<ID> findAncestorIds(ID currentId) {
    Set ids = Sets.newHashSet();
    M m = findById(currentId);
    if (m == null) {
      return ids;
    }
    for (String idStr : StringUtils.tokenizeToStringArray(m.getParentIds(), "/")) {
      if (!StringUtils.isEmpty(idStr)) {
        ids.add(Long.valueOf(idStr));
      }
    }
    return ids;
  }

  /**
   * 递归查询祖先
   * 
   * @param parentIds
   * @return
   */
  public List<M> findAncestor(String parentIds) {
    if (StringUtils.isEmpty(parentIds)) {
      return Collections.emptyList();
    }
    String[] ids = StringUtils.tokenizeToStringArray(parentIds, "/");
    List<M> mList = Lists.newArrayList();
    for (String id : ids) {
      mList.addAll(findAllWithSort(Searchable.newSearchable().addSearchParam("id", id)));
    }
    return Lists.reverse(mList);
  }

  public List<M> removeExcludeSearch(List<M> mList, M excludeM) {
    List<M> resList = new ArrayList<>();
    if (excludeM == null) {
      return null;
    }
    for (M m : mList) {
      if (!m.getId().equals(excludeM.getId())) {
        resList.add(m);
        continue;
      }
      if (!m.getParentIds().endsWith(excludeM.makeSelfAsNewParentIds())) {
        resList.add(m);
        continue;
      }
    }
    return resList;
  }

  /**
   * 按条件排序查询实体(不分页)
   * 
   * @param searchable
   *          条件
   * @return
   */
  public List<M> findAllWithSort(Searchable searchable) {
    List<M> pi = findList(searchable);
    return pi;

  }

  /**
   * 按条件不分页不排序查询实体
   * 
   * @param searchable
   *          条件
   * @return
   */
  public List<M> findAllWithNoPageNoSort(Searchable searchable) {
    searchable.removePageable();
    searchable.removeSort();
    List<M> list = findList(searchable);
    return  list;
  }

  // 从list中排除特定元素
  public List<M> addExcludeSearchFilter(List<M> allM, M excludeM) {
    if (allM == null || excludeM == null) {
      return null;
    }
    List<M> resMs = new ArrayList<>();
    for (M m : allM) {
      if (!m.equals(excludeM)) {
        modifyParentFlagBeforeDelete(m.getParentId());
        resMs.add(m);
      }
    }
    return resMs;
  }
}

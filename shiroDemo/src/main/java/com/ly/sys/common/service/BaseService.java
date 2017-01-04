package com.ly.sys.common.service;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.sys.common.dao.BaseDao;
import com.ly.sys.common.entity.AbstractEntity;
import com.ly.sys.common.entity.search.Searchable;

/**
 * <p>Description:
 * <p>User: mtwu
 * <p>Date: 2016-6-27
 * <p>Version: 1.0
 */
@Service
public abstract class BaseService<M extends AbstractEntity, ID extends Serializable> {
  @Autowired
  protected BaseDao<M, ID> baseDao;
  

  public void setBaseDao(BaseDao<M, ID> baseDao) {
    this.baseDao = baseDao;
  }

  public PageInfo<M> findPageList(Searchable searchable) {
    PageHelper.startPage(searchable.getPage().getPageNumber(), searchable.getPage().getPageSize());
    Sort sort = searchable.getSort();
    Iterator<Sort.Order> it = sort.iterator();
    StringBuilder sortStr = null;
    if(it.hasNext()) sortStr = new StringBuilder();
    while (it.hasNext()) {
      Sort.Order order = it.next();
      sortStr.append(",T.").append(order.getProperty()).append(" ").append(order.getDirection().toString());
    }
    PageHelper.orderBy(sortStr.substring(1));
    List<M> list = baseDao.findPageList(searchable.getSearchFilters());
    PageInfo<M> pi = new PageInfo<M>(list);
    return pi;
  }
  public List<M> findList(Searchable searchable) {
    int size = baseDao.findCount(searchable.getSearchFilters());
    PageHelper.startPage(1, size);
    Sort sort = searchable.getSort();
    if(null != sort){
      
      Iterator<Sort.Order> it = sort.iterator();
      StringBuilder sortStr = null;
      if(it.hasNext()) sortStr = new StringBuilder();
      while (it.hasNext()) {
        Sort.Order order = it.next();
        sortStr.append(",T.").append(order.getProperty()).append(" ").append(order.getDirection().toString());
      }
      PageHelper.orderBy(sortStr.substring(1));
    }
    List<M> list = baseDao.findPageList(searchable.getSearchFilters());
    PageInfo<M> pi = new PageInfo<M>(list);
    return pi.getList();
  }

  public M findById(ID id) {
    M m = baseDao.findById(id);
    return m;
  }

  public M add(M m) {
    baseDao.insert(m);
    return m;
  }

  public M update(M m) {
    baseDao.update(m);
    return m;
  }

  @SuppressWarnings("unchecked")
  public boolean save(M mForm){
    M m = null;
    if ( mForm.getId() == null ) {
        m = mForm;
      } else {
        m =findById((ID)mForm.getId());
    }
    if(m!=null){
      copyProperties(m, mForm);
      
      if ( m.getId() != null) {
        update(m);
      } else {
        add(m);
      }
      mForm = m;
      return true;
    }else{
      add(mForm);
      return true;
    }
  }

  public void delete(ID[] ids) {
    Integer len = ids.length;
    for (int i = 0; i < len; i++) {
      ID id = ids[i];
      if (null != id) {
        delete(id);
      }
    }
  }

  public void delete(ID id) {
    baseDao.delete(id);
  }

  public abstract void copyProperties(M dest, M orig);
}

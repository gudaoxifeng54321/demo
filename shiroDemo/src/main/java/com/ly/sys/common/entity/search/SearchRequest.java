package com.ly.sys.common.entity.search;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.ly.sys.common.entity.search.exception.SearchException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * <p>
 * 查询条件（包括分页和排序）
 * </p>
 * <p>
 * User: mtwu
 * <p>
 * Date: 13-1-15 上午7:29
 * <p>
 * Version: 1.0
 */

public final class SearchRequest extends Searchable {

  private final Map<String, Object> searchFilterMap = Maps.newHashMap();
  
  private final List<SearchFilter> searchFilters = Lists.newArrayList();
  private Pageable                  page;

  private Sort                      sort;

  private boolean                   converted;

  /**
   * @param searchParams
   * @see SearchRequest#SearchRequest(java.util.Map<java.lang.String,java.lang.Object>)
   */
  public SearchRequest(final Map<String, Object> searchParams) {
    this(searchParams, null, null);
  }

  public SearchRequest() {
    this(null, null, null);
  }

  /**
   * @param searchParams
   * @see SearchRequest#SearchRequest(java.util.Map<java.lang.String,java.lang.Object>)
   */
  public SearchRequest(final Map<String, Object> searchParams, final Pageable page) {
    this(searchParams, page, null);
  }

  /**
   * @param searchParams
   * @see SearchRequest#SearchRequest(java.util.Map<java.lang.String,java.lang.Object>)
   */
  public SearchRequest(final Map<String, Object> searchParams, final Sort sort) {
    this(searchParams, null, sort);
  }

  public SearchRequest(final Map<String, Object> searchParams, final Pageable page, final Sort sort) {
    if (searchParams != null && searchParams.size() > 0) {
      searchFilterMap.putAll(searchParams);
    }
    merge(sort, page);
  }

  @Override
  public Map<String, Object> getSearchFilters() {
    return searchFilterMap;
  }

  @Override
  public Searchable addSearchParam(final String key, final Object value) {
    searchFilterMap.put(key, value);
    return this;
  }

  @Override
  public Searchable addSearchParams(Map<String, Object> searchParams) {
    searchFilterMap.putAll(searchParams);
    return this;
  }

  @Override
  public Searchable setPage(final Pageable page) {
    merge(sort, page);
    return this;
  }

  @Override
  public Searchable setPage(int pageNumber, int pageSize) {
    merge(sort, new PageRequest(pageNumber, pageSize));
    return this;
  }

  @Override
  public Searchable addSort(final Sort sort) {
    merge(sort, page);
    return this;
  }

  @Override
  public Searchable addSort(final Sort.Direction direction, final String property) {
    merge(new Sort(direction, property), page);
    return this;
  }

  /**
   * @param key
   * @return
   */
  @Override
  public Searchable removeSearchFilter(final String key) {
    if (key == null) {
      return this;
    }
    searchFilterMap.remove(key);

    return this;
  }

  @Override
  public boolean hasSearchFilter() {
    return searchFilterMap.size() > 0;
  }
  
  @Override
  public boolean hashSort() {
      return this.sort != null && this.sort.iterator().hasNext();
  }

  @Override
  public boolean hasPageable() {
      return this.page != null && this.page.getPageSize() > 0;
  }

  @Override
  public void removeSort() {
      this.sort = null;
      if (this.page != null) {
          this.page = new PageRequest(page.getPageNumber(), page.getPageSize(), null);
      }
  }

  @Override
  public void removePageable() {
      this.page = null;
  }

  public Pageable getPage() {
      return page;
  }

  public Sort getSort() {
      return sort;
  }
  
  @Override
  public boolean containsSearchKey(String key) {
    boolean contains = searchFilterMap.containsKey(key);

    return true;
  }

  @Override
  public Object getValue(String key) {
    Object searchFilter = searchFilterMap.get(key);

    return searchFilter;
  }

  private void merge(Sort sort, Pageable page) {
    if (sort == null) {
      sort = this.sort;
    }
    if (page == null) {
      page = this.page;
    }

    // 合并排序
    if (sort == null) {
      this.sort = page != null ? page.getSort() : null;
    } else {
      this.sort = (page != null ? sort.and(page.getSort()) : sort);
    }
    // 把排序合并到page中
    if (page != null) {
      this.page = new PageRequest(page.getPageNumber(), page.getPageSize(), this.sort);
    } else {
      this.page = null;
    }
  }
  
  @Override
  public String toString() {
    return "SearchRequest{" + "searchFilterMap=" + searchFilterMap + '}';
  }

  @Override
  public Searchable addSearchFilter(String searchProperty, SearchOperator operator, Object value) throws SearchException {
    SearchFilter searchFilter = SearchFilterHelper.newCondition(searchProperty, operator, value);
    return addSearchFilter(searchFilter);
    
  }
  @Override
  public Searchable addSearchFilter(SearchFilter searchFilter) {
      if (searchFilter == null) {
          return this;
      }
      if (searchFilter instanceof Condition) {
          Condition condition = (Condition) searchFilter;
          String key = condition.getKey();
          searchFilterMap.put(key, condition);
      }
      int index = searchFilters.indexOf(searchFilter);
      if(index != -1) {
          searchFilters.set(index, searchFilter);
      } else {
          searchFilters.add(searchFilter);
      }
      return this;

  }
   
}

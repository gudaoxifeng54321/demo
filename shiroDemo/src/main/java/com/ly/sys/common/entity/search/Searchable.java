package com.ly.sys.common.entity.search;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.ly.sys.common.entity.search.exception.SearchException;





/**
 * <p>查询条件接口</p>
 * <p>User: mtwu
 * <p>Date: 13-1-16 上午8:47
 * <p>Version: 1.0
 */
public abstract class Searchable {


    /**
     * 创建一个新的查询
     *
     * @return
     */
    public static Searchable newSearchable() {
        return new SearchRequest();
    }

    /**
     * 创建一个新的查询
     *
     * @return
     */
    public static Searchable newSearchable(final Map<String, Object> searchParams){
        return new SearchRequest(searchParams);
    }
    public abstract Searchable addSearchFilter(final SearchFilter searchFilter);
    
    public abstract Searchable addSearchParam(final String key, final Object value);
    
    public abstract Searchable addSearchParams(Map<String, Object> searchParams);

    public abstract Map<String,Object> getSearchFilters();
    
    public abstract Searchable removeSearchFilter(final String key);

    /**
     * 是否有查询参数
     *
     * @return
     */
    public abstract boolean hasSearchFilter();

    
 
    public abstract boolean containsSearchKey(String key);

    

    public abstract Object getValue(String key);

    public abstract Searchable setPage(Pageable page);

    public abstract Searchable setPage(int pageNumber, int pageSize);

    public abstract Searchable addSort(Sort sort);

    public abstract Searchable addSort(Direction direction, String property);
    
    

    /**
     * 是否有排序
     *
     * @return
     */
    public abstract boolean hashSort();

    public abstract void removeSort();

    /**
     * 是否有分页
     *
     * @return
     */
    public abstract boolean hasPageable();

    public abstract void removePageable();

    /**
     * 获取分页和排序信息
     *
     * @return
     */
    public abstract Pageable getPage();

    /**
     * 获取排序信息
     *
     * @return
     */
    public abstract Sort getSort();
    
    
    /**
     * 添加过滤条件
     *
     * @param searchProperty 查询的属性名
     * @param operator       操作运算符
     * @param value          值
     */
    public abstract Searchable addSearchFilter(
            final String searchProperty, final SearchOperator operator, final Object value) throws SearchException;
}

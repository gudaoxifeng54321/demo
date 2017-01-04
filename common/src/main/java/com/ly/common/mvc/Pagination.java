package com.ly.common.mvc;
/**
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source
 * code file is protected by copyright law and international treaties.
 * Unauthorized distribution of source code files, programs, or portion of the
 * package, may result in severe civil and criminal penalties, and will be
 * prosecuted to the maximum extent under the law.
 * 
 * 分页查询之分页信息类
 * 
 * @author gaofeng
 * @since 2015-11-2
 */
public final class Pagination {
  private int pageNum        = 1; // 当前页码
  private int pageSize       = 20; // 每页size 默认20
  private int navigationSize = 9; // 分页条相邻页数量 默认9

  public int getPageNum() {
    return pageNum;
  }

  public void setPageNum(int pageNum) {
    this.pageNum = pageNum;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getNavigationSize() {
    return navigationSize;
  }

  public void setNavigationSize(int navigationSize) {
    this.navigationSize = navigationSize;
  }

}

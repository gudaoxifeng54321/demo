package com.ly.common.json;



import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source code file is protected by copyright law and international
 * treaties. Unauthorized distribution of source code files, programs, or portion of the package, may result in severe civil and criminal
 * penalties, and will be prosecuted to the maximum extent under the law.
 * 
 * Gson实现的 json 转化器, 可以 Object <====> json 相互转化
 * 
 * @author gaofeng
 * @since 2015-10-30
 */
public final class Json {
  private final static Gson gson                          = new GsonBuilder().serializeNulls().setPrettyPrinting()
                                                              .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
  private final static Gson gsonPlain                     = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")
                                                              .create();
  private final static Gson gson2Null                     = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
  private final static Gson gsonExcludeExpose             = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")
                                                              .excludeFieldsWithoutExposeAnnotation().create();                     // 屏蔽字段用的gson

  private final static Gson gsonWithSimpleDate              = new GsonBuilder().serializeNulls().setPrettyPrinting()
                                                              .setDateFormat("yyyy-MM-dd").create();
  private final static Gson gsonPlainWithSimpleDate         = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
  private final static Gson gson2NullWithSimpleDate         = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
  private final static Gson gsonExcludeExposeWithSimpleDate = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd")
                                                              .excludeFieldsWithoutExposeAnnotation().create();                     // 屏蔽字段用的gson

  public static String toJson(Object o) {

    return gson.toJson(o);
  }

  public static String toJsonWithSimpleDate(Object o) {

    return gsonWithSimpleDate.toJson(o);
  }

  /**
   * 当结果为null时，不返回键值对
   * 
   * @param o
   * @return
   */

  public static String toJson2Null(Object o) {
    return gson2Null.toJson(o);
  }

  public static String toJson2NullWithSimpleDate(Object o) {
    return gson2NullWithSimpleDate.toJson(o);
  }

  /**
   * 返回未格式化的json
   * 
   * @param o
   * @return
   */
  public static String toJsonPlain(Object o) {
    return gsonPlain.toJson(o);
  }
  public static String toJsonPlainWithSimpleDate(Object o) {
    return gsonPlainWithSimpleDate.toJson(o);
  }
  public static Object fromJson(String json, Class<? extends Object> c) {
    return gson.fromJson(json, c);
  }

  public static Object fromJson(String json, Type t) {
    return gson.fromJson(json, t);
  }

  public static String toJsonExclodeExpose(Object o) {
    return gsonExcludeExpose.toJson(o);
  }
  public static String toJsonExclodeExposeWithSimpleDate(Object o) {
    return gsonExcludeExposeWithSimpleDate.toJson(o);
  }
}

package com.ly.common.upload.imageUpload;

import java.io.File;

import com.ly.common.Constant;
import com.ly.common.encrypt.MD5;

public final class UploadImagePath implements Constant {
  public static final String ROOT = System.getProperty("catalina.home") + File.separator + "images";
  
  public static final String PHOTOGRAPHY = "photography";
  public static final String MODEL = "model";
  
  public static final String SHOP = "shop";
  
  public static final String ID = "ID";
  public static final String WORK = "work";
  
  public static final String POSITIVE = "positive";
  public static final String NEGATIVE = "negative";
  public static final String HANDLED = "handled";
  
  public static final String BUSINESS_LICENSE = "business license";
  
  public static final String PHOTOGRAPHY_ID_POSITIVE_IMAGE = ROOT + File.separator + PHOTOGRAPHY + File.separator + ID + File.separator + POSITIVE; 
  public static final String PHOTOGRAPHY_ID_NEGATIVE_IMAGE = ROOT + File.separator + PHOTOGRAPHY + File.separator + ID + File.separator + POSITIVE;
  public static final String PHOTOGRAPHY_WORK_IMAGE = ROOT + File.separator + PHOTOGRAPHY + File.separator + WORK;
  
  public static final String PHOTOGRAPHY_SHOP_BUSINESS_LICENSE = ROOT + File.separator + PHOTOGRAPHY +File.separator + SHOP + File.separator + BUSINESS_LICENSE;
  
  /**
   * 根据字符串,返回MD5过滤后的字符串
   * 
   * @param name
   * @return
   * @throws Exception 
   */
  public static String generateImageName (String name, String suffix) throws Exception {
    return MD5.md5Encode(name) + "." + suffix;
  }
  
  public static String generatingARandomString () {
    return null;
  }
}

package com.ly.common.cloud;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.common.X;
import com.ly.common.json.Json;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

/**
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source code file is protected by copyright law and international
 * treaties. Unauthorized distribution of source code files, programs, or portion of the package, may result in severe civil and criminal
 * penalties, and will be prosecuted to the maximum extent under the law.
 * 
 * 七牛上传文件 工具类
 * 
 * @author gaofeng
 * @since 2015-12-21
 */
public class SevenBullUploader {
  private final static Logger log = LoggerFactory.getLogger(SevenBullUploader.class);
  private Auth                auth;
  private UploadManager       um  = new UploadManager();

  /**
   * 创建实例必须指定 accessKey 和secretKey
   * 
   * @param accessKey
   * @param secretKey
   */
  public SevenBullUploader(String accessKey, String secretKey) {
    auth = Auth.create(accessKey, secretKey);
  }

  /**
   * 上传一个文件
   * 
   * @param bucket
   *          上传到指定桶
   * @param key
   *          指定上传文件的key (可以是虚拟目录 如 /product/0/2015/12/12/001.jpg)
   * @param file
   *          要上传的file对象
   */
  public boolean upload(String bucket, String key, File file) {
    String token = auth.uploadToken(bucket, key);
    try {
      Response r = um.put(file, key, token);
      if (r.isOK()) {
        return X.TRUE;
      }
    } catch (QiniuException e) {
      Response r = e.response;
      System.out.println("返回:"+Json.toJson(r));
      log.error(null, e);
      try {
        log.error(r.bodyString());
      } catch (QiniuException e1) {
      }
    }
    return X.FALSE;
  }

  /**
   * 上传一个字节数组
   * 
   * @param bucket
   *          上传到指定桶
   * @param key
   *          指定上传文件的key (可以是虚拟目录 如 /product/0/2015/12/12/001.jpg)
   * @param content
   *          要上传的文件所读取出的 byte[]
   */
  public boolean upload(String bucket, String key, byte[] content) {
    String token = auth.uploadToken(bucket, key);
    try {
      Response r = um.put(content, key, token);
      if (r.isOK()) {
        return X.TRUE;
      }
    } catch (QiniuException e) {
      Response r = e.response;
      log.error(null, e);
      try {
        log.error(r.bodyString());
      } catch (QiniuException e1) {
      }
    }
    return X.FALSE;
  }
  
  /**
   * 删除一个文件
   * 
   * @param bucket
   *          上传到指定桶
   * @param key
   *          指定上传文件的key (可以是虚拟目录 如 /product/0/2015/12/12/001.jpg)
   * @param file
   *          要上传的file对象
   */
  public boolean delete(String bucket, String key) {
    BucketManager bucketManager = new BucketManager(auth);
    try {
      bucketManager.delete(bucket, key);
    } catch (QiniuException e) {
      Response r = e.response;
      log.error("返回:"+Json.toJson(r));
      log.error(null, e);
      try {
        log.error(r.bodyString());
        } catch (QiniuException e1) {
      }
    }
    return X.TRUE;
  }
  
}

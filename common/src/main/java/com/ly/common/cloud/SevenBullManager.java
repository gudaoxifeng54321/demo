package com.ly.common.cloud;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.common.X;
import com.ly.common.io.FileUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;

/**
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source code file is protected by copyright law and international
 * treaties. Unauthorized distribution of source code files, programs, or portion of the package, may result in severe civil and criminal
 * penalties, and will be prosecuted to the maximum extent under the law.
 * 
 * 七牛管理线上资源 工具类
 * 
 * @author gaofeng
 * @since 2015-12-21
 */
public class SevenBullManager {
  private final static Logger log = LoggerFactory.getLogger(SevenBullUploader.class);
  private Auth                auth;
  private BucketManager       bm;

  /**
   * 创建实例必须指定 accessKey 和secretKey
   * 
   * @param accessKey
   * @param secretKey
   */
  public SevenBullManager(String accessKey, String secretKey) {
    auth = Auth.create(accessKey, secretKey);
    bm = new BucketManager(auth);
  }

  /**
   * 查询本账号下的bucket列表
   * 
   * @return String[] bucket数组
   */
  public String[] getBuckets() {
    try {
      return bm.buckets();
    } catch (QiniuException e) {
      log.error(null, e);
      return null;
    }
  }

  /**
   * 遍历bucket中的内容, 把key和fileSize 存入 队列
   * 
   * @param bucket
   * @param prefix
   * @param limit
   */
  public long fetchResourceFromBucket(String bucket, String prefix, int limit, ConcurrentHashMap<String, SevenBullClouldFileInfo> map) {
    BucketManager.FileListIterator it = bm.createFileListIterator(bucket, prefix, limit, null);
    long count = 0;
    while (it.hasNext()) {
      FileInfo[] items = it.next();
      for (FileInfo fi : items) {
        count++;
        if(count%10000==0){
          log.debug("!!!!mapSize : {}",count);
        }
        map.put(fi.key, new SevenBullClouldFileInfo(fi.key, fi.fsize, fi.mimeType, fi.putTime));
      }
    }
    return count;
  }

  /**
   * 遍历bucket中的内容, 批量全部删除
   * 
   * @param bucket
   * @param prefix
   * @param limit
   */
  public void deleteAll(String bucket) {
    ConcurrentHashMap<String, SevenBullClouldFileInfo> map = new ConcurrentHashMap<String, SevenBullClouldFileInfo>();
    fetchResourceFromBucket(bucket, "", 1000, map);
    Set<String> keys = map.keySet();
    BucketManager.Batch ops = new BucketManager.Batch();
    for (String k : keys) {
      map.get(k);
      ops.delete(bucket, map.get(k).key);
    }
    // .copy(TestConfig.bucket, TestConfig.key, TestConfig.bucket, key)
    // .move(TestConfig.bucket, key1, TestConfig.bucket, key2)
    // .rename(TestConfig.bucket, key3, key4)
    // .stat(TestConfig.bucket, array)
    // .stat(TestConfig.bucket, array[0])
    // .stat(TestConfig.bucket, array[1], array[2])

    try {
      Response r = bm.batch(ops);
      BatchStatus[] bs = r.jsonToObject(BatchStatus[].class);
      for (BatchStatus b : bs) {
        System.out.println(b.code);
      }
    } catch (QiniuException e) {
      Response r = e.response;
      // 请求失败时简单状态信息
      log.error(r.toString());
      try {
        // 响应的文本信息
        log.error(r.bodyString());
      } catch (QiniuException e1) {
        // ignore
      }
    }
  }

  /**
   * 删除指定bucket下 指定key 不可调用超过100次 否则异常 大批量删除需要用批量操作
   * 
   * @param bucket
   * @param key
   */
  public void delete(String bucket, String key) {
    try {
      bm.delete(bucket, key);
    } catch (QiniuException e) {
      log.error(null, e);
    }
  }

  /**
   * 生成缩放并另存线上图片的URL
   * 
   * @param domain
   *          源图片所在域名
   * @param originKey
   *          源图片key
   * @param targetWidth
   *          目标图片宽度, 高度会等比自动缩放
   * @param targetQuality
   *          目标图片的品质, 0~100 70以上晃眼看不出来.
   * @param targetBucket
   *          目标图片的存储bucket
   * @return 样例: <<< 7xkq77.com2.z0.glb.qiniucdn.com/0/100030/20141011/20141011121002638.jpg?imageView2/2/w/750/q/90|saveas/
   *         Z290d28tdGh1bWJuYWlsOjAvMTAwMDMwLzIwMTQxMDExLzIwMTQxMDExMTIxMDAyNjM4Xzc1MC5qcGc
   *         =/sign/UIgBw7sNUlZ1ANb0GpLeydDrY8T59cpRGTDut0c2:IsSqErpuSFX96KOaIoTaXFdZOmg= >>>
   */
  public String getScaleUrl(String domain, String originKey, int targetWidth, int targetQuality, String targetBucket) {
    StringBuilder encodedEntryURI = new StringBuilder(targetBucket);
    encodedEntryURI.append(":").append(originKey.substring(0, originKey.lastIndexOf(".")));
    encodedEntryURI.append("_").append(targetWidth).append(originKey.substring(originKey.lastIndexOf(".")));
    StringBuilder sb = new StringBuilder(domain);
    sb.append(originKey).append("?imageView2/2/w/").append(targetWidth).append("/q/").append(targetQuality);
    sb.append("|saveas/");
    sb.append(UrlSafeBase64.encodeToString(encodedEntryURI.toString()));
    String signed = auth.sign(sb.toString());
    sb.append("/sign/").append(signed);
    sb.append("&v=").append(X.RANDOM.nextInt(100));
    return sb.toString();
  }

  /**
   * 生成 转存bucket的url
   * 
   * @param domain
   *          源图片所在域名
   * @param originKey
   *          源图片key
   * @param targetWidth
   *          目标图片宽度, 高度会等比自动缩放
   * @param targetBucket
   *          目标图片的存储bucket
   * @return 样例: <<< 7xkq77.com2.z0.glb.qiniucdn.com/0/100030/20141011/20141011121002638.jpg?imageView2/2/w/750/q/90|saveas/
   *         Z290d28tdGh1bWJuYWlsOjAvMTAwMDMwLzIwMTQxMDExLzIwMTQxMDExMTIxMDAyNjM4Xzc1MC5qcGc
   *         =/sign/UIgBw7sNUlZ1ANb0GpLeydDrY8T59cpRGTDut0c2:IsSqErpuSFX96KOaIoTaXFdZOmg= >>>
   */
  public String getCloneUrl(String domain, String originKey, int targetWidth, String targetBucket) {
    StringBuilder encodedEntryURI = new StringBuilder(targetBucket);
    encodedEntryURI.append(":").append(originKey.substring(0, originKey.lastIndexOf(".")));
    encodedEntryURI.append("_").append(targetWidth).append(originKey.substring(originKey.lastIndexOf(".")));
    StringBuilder sb = new StringBuilder(domain);
    sb.append(originKey);
    sb.append("?saveas/");
    sb.append(UrlSafeBase64.encodeToString(encodedEntryURI.toString()));
    String signed = auth.sign(sb.toString());
    sb.append("/sign/").append(signed);
    sb.append("&v=").append(X.RANDOM.nextInt(100));
    return sb.toString();
  }

  public void compare(ConcurrentHashMap<String, SevenBullClouldFileInfo> mapA, ConcurrentHashMap<String, SevenBullClouldFileInfo> mapB) {
    String prefix, suffix, s750, s450, s220;
    Set<String> keys = mapA.keySet();
    SevenBullClouldFileInfo sbcfi;
    int fa=0;
    for (String k : keys) {
      if((fa++)%1000==0){
        log.debug("########################################## {}",fa);
      }
      sbcfi = mapA.get(k);
      changeMimeType(sbcfi, "go2mm-imagefiles");
      boolean s7 = false, s4 = false, s2 = false;
      prefix = sbcfi.key.substring(0, sbcfi.key.lastIndexOf("."));
      suffix = sbcfi.key.substring(sbcfi.key.lastIndexOf("."));
      s750 = new StringBuilder(prefix).append("_").append(750).append(suffix).toString();
      s450 = new StringBuilder(prefix).append("_").append(450).append(suffix).toString();
      s220 = new StringBuilder(prefix).append("_").append(220).append(suffix).toString();
      if (mapB.containsKey(s750)) {
        changeMimeType(mapB.get(s750), "go2mm-thumbnail");
        s7 = true;
        mapB.remove(s750);
      }
      if (mapB.containsKey(s450)) {
        changeMimeType(mapB.get(s450), "go2mm-thumbnail");
        s4 = true;
        mapB.remove(s450);
      }
      if (mapB.containsKey(s220)) {
        changeMimeType(mapB.get(s220), "go2mm-thumbnail");
        s2 = true;
        mapB.remove(s220);
      }
      if (s7 && s4 && s2) {
        mapA.remove(k);
        continue;
      }
      if (!s7&&sbcfi.size>1000) {
        log.info("{} ____size : {}  >>>>>>>>> 750   missing", sbcfi.key, sbcfi.size);
      }
      if (!s4&&sbcfi.size>1000) {
        log.info("{} ____size : {}  >>>>>>>>> 450   missing", sbcfi.key, sbcfi.size);
      }
      if (!s2&&sbcfi.size>1000) {
        log.info("{} ____size : {}  >>>>>>>>> 220   missing", sbcfi.key, sbcfi.size);
      }
    }
  }

  private void changeMimeType(SevenBullClouldFileInfo sbcfi, String bucket) {
    String mime = sbcfi.mimeType;
    String ext = FileUtil.getExtension(sbcfi.key);
    try {
      if ("jpg".equals(ext) || "jpeg".equals(ext)) {
        if (!"image/jpeg".equals(mime)) {
          bm.changeMime(bucket, sbcfi.key, "image/jpeg");
          log.info("@@@@@@@@@@  {}/{} @@  MIME FROM {} TO image/jpeg  ",bucket, sbcfi.key, mime);
        }
      } else if ("png".equals(ext)) {
        if (!"image/png".equals(mime)) {
          bm.changeMime(bucket, sbcfi.key, "image/png");
          log.info("@@@@@@@@@@ {}/{} @@  MIME FROM {} TO image/png  ",bucket, sbcfi.key, mime);
        }
      } else if ("gif".equals(ext)) {
        if (!"image/gif".equals(mime)) {
          bm.changeMime(bucket, sbcfi.key, "image/gif");
          log.info("@@@@@@@@@@ {}/{} @@  MIME FROM {} TO image/gif  ",bucket, sbcfi.key, mime);
        }
      } else if ("g".equals(ext) && !"image/jpeg".equals(mime)) {
        bm.changeMime(bucket, sbcfi.key, "image/jpeg");
        log.info("@@@@@@@@@@ {}/{} @@  MIME FROM {} TO image/jpeg  ",bucket, sbcfi.key, mime);
      }
    } catch (Exception e) {
      log.error(null, e);
    }
  }

  /**
   * 从命名文件 将key中的 "!" 用 "_" 替代.
   * 
   * @param srcBucket
   * @param key
   * @return
   */
  public String rename(String srcBucket, String key) {
    log.info("RENAMING {}:{}", srcBucket, key);
    if (null == bm) {
      log.error("@@@@@@@@ bm is null");
      bm = new BucketManager(auth);
    }
    String newKey = key.replaceAll("!", "_");
    try {
      bm.move(srcBucket, key, srcBucket, newKey);
      return newKey;
    } catch (Exception e) {
      log.error(null, e);
      return newKey;
    }
  }

  /**
   * 检查资源的状态属性
   * 
   * @param bucket
   * @param key
   * @return
   */
  public SevenBullClouldFileInfo stat(String bucket, String key) {
    FileInfo fi;
    SevenBullClouldFileInfo sbcfi = null;
    try {
      fi = bm.stat(bucket, key);
      sbcfi = new SevenBullClouldFileInfo(fi.key, fi.fsize, fi.mimeType, fi.putTime);
    } catch (QiniuException e) {
      log.error("Key {}:{} doesn't exist ", bucket, key);
    }
    return sbcfi;
  }
}

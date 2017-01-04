package com.ly.common.page;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.common.X;

public final class Http {
  private final static Logger log          = LoggerFactory.getLogger(Http.class);
  public static String        charset      = "gbk";
  private static int          countForPage = 15;
  CloseableHttpClient         client;

  private synchronized void checkFowSleep() {
    if (--countForPage == 0) {
      int s = X.RANDOM.nextInt(5);
      log.info("SLEEP FOR : " + (10 + s) + "seconds");
      X.sleep(s);
      countForPage = 15;
    }
  }

  public Http(String host, int maxTotal, int DefaultMaxPerRoute, int TargetMaxPerRoute) {
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    // 将最大连接数增加到maxTotal
    cm.setMaxTotal(maxTotal);
    // 将每个路由基础的连接增加到MaxPerRoute
    cm.setDefaultMaxPerRoute(DefaultMaxPerRoute);
    // 将目标主机的最大连接数增加到50
    cm.setMaxPerRoute(new HttpRoute(new HttpHost(host)), TargetMaxPerRoute);
    client = HttpClients.custom().setConnectionManager(cm).build();
  }

  public boolean multipleDownload(String url, String file) {
    checkFowSleep();
    File targetFile = new File(file);
    if (targetFile.exists()) {
      log.info(targetFile.getAbsolutePath() + " EXIST!! will be covered");
    }
    log.info("  HTTP download | " + url + " >> " + file);
    long p = System.currentTimeMillis();
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    CloseableHttpResponse response = null;
    try {
      HttpGet get = createTimeOutGet(url);
      response = client.execute(get);
      if (response != null) {
        bis = new BufferedInputStream(response.getEntity().getContent());
        bos = new BufferedOutputStream(new FileOutputStream(file));
        byte b[] = new byte[1024 * 1024];
        for (int l = -1; (l = bis.read(b)) != -1;) {
          bos.write(b, 0, l);
          bos.flush();
        }
        bos.close();
        bis.close();
        response.close();
      }
      p = System.currentTimeMillis() - p;
      log.info("    End download        " + p + "ms        " + file);
    } catch (Exception e) {
      log.error(e.getMessage() + url);
      return false;
    } finally {
      try {
        bis.close();
        bos.close();
        if (response != null) {
          response.close();
        }
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
    return true;
  }

  public static HttpResult get(String url) {
    log.info((new StringBuilder("GET : ")).append(url).toString());
    CloseableHttpClient client = HttpClients.createDefault();
    HttpResult result = null;
    try {
      HttpResponse response = client.execute(createGet(url));
      if (response != null)
        result = new HttpResult(response, X.stream2String(response.getEntity().getContent(), charset));
      client.close();
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static boolean download(String url, String file) {
    File targetFile = new File(file);
    if (targetFile.exists()) {
      log.info(targetFile.getAbsolutePath() + " EXIST!!");
      return true;
    }
    log.info("HTTP download | " + url + " >> " + file);
    CloseableHttpClient client = HttpClients.createDefault();
    long p = System.currentTimeMillis();
    try {
      HttpGet get = createImageGet(url);
      HttpResponse response = client.execute(get);
      if (response != null) {
        BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        byte b[] = new byte[81920];
        for (int l = -1; (l = bis.read(b)) != -1;) {
          bos.write(b, 0, l);
          bos.flush();
        }
        bos.close();
        bis.close();
      }
      client.close();
      // returnGet(get);
      p = System.currentTimeMillis() - p;
      log.info("End download        " + p + "ms        " + url);
    } catch (ClientProtocolException e) {
      log.error(e.getMessage());
      return false;
    } catch (IOException e) {
      log.error(e.getMessage());
      return false;
    }
    return true;
  }

  private static HttpGet createTimeOutGet(String url) {
    HttpGet get = createImageGet(url);
    RequestConfig rc = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).build();
    // 设置请求和传输超时时间
    get.setConfig(rc);
    return get;
  }

  private static HttpGet createGet(String url) {
    HttpGet get = new HttpGet(url);
    get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    get.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
    get.setHeader("User-Agent",
        "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
    get.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
    return get;
  }

  private static HttpGet createImageGet(String url) {
    HttpGet get = new HttpGet(url);
    get.setHeader("Accept", "image/webp,*/*;q=0.8");
    get.setHeader("Host", getHost(url));
    get.setHeader("Accept-Encoding", "gzip,deflate,sdch");
    get.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
    get.setHeader("Cache-Control", "keep-alive");
    get.setHeader("Connection", "close");
    get.setHeader("Pragma", "no-cache");
    get.setHeader("User-Agent",
        "User-Agent:Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36");
    return get;
  }

  public static String getHost(String url) {
    URI uri = URI.create(url);
    return uri.getHost();
  }
}

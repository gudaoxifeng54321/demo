package com.ly.common.page;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ly.common.X;

@Deprecated
public class HttpAgent {
  private final static Logger log = LoggerFactory.getLogger(HttpAgent.class);

  public HttpAgent(String charset) {
    this.charset = charset;
    client = HttpClients.createDefault();
  }

  public HttpResult get(String url) {
    return get(url, null);
  }

  public HttpResult get(String url, Header headers[]) {
    log.info((new StringBuilder("HTTP GET : ")).append(url).toString());
    HttpGet get = new HttpGet(url);
    wrapAsChrome(get);
    if (headers != null) {
      Header aheader[];
      int j = (aheader = headers).length;
      for (int i = 0; i < j; i++) {
        Header h = aheader[i];
        get.addHeader(h);
      }

    }
    try {
      HttpResponse response = client.execute(get);
      if (response != null)
        return new HttpResult(response, X.stream2String(response.getEntity().getContent(), charset));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void destroy() {
    if (client != null)
      try {
        client.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
  }

  public boolean fetchFile(String url, String file) {
    File targetFile = new File(file);
    if (targetFile.exists()) {
      log.info((new StringBuilder(String.valueOf(targetFile.getAbsolutePath()))).append("   EXIST!!").toString());
      return true;
    }
    log.info((new StringBuilder("Fetch | ")).append(url).append(" >> ").append(file).toString());
    HttpGet get = new HttpGet(url);
    wrapAsChrome(get);
    try {
      HttpResponse response = client.execute(get);
      if (response != null) {
        BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        byte b[] = new byte[8192];
        for (int l = -1; (l = bis.read(b)) != -1;) {
          bos.write(b, 0, l);
          bos.flush();
        }

        bos.close();
        bis.close();
      }
    } catch (ClientProtocolException e) {
      log.error(e.getMessage());
      return false;
    } catch (IOException e) {
      log.error(e.getMessage());
      return false;
    }
    return true;
  }

  private void wrapAsChrome(HttpRequestBase hrb) {
    hrb.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    hrb.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
    hrb.setHeader("User-Agent",
        "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
    hrb.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
    hrb.setHeader("Connection", "close");
  }

  public static final String    OK        = "HTTP/1.1 200 OK";
  public static final String    MOVED     = "HTTP/1.1 302 Moved";
  public static final String    NOT_FOUND = "HTTP/1.1 302 Found";
  protected String              charset;
  protected CloseableHttpClient client;

}

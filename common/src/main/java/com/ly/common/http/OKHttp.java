package com.ly.common.http;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by gaofeng on 17/10/2016.
 */
public class OKHttp {
  private Logger log = LoggerFactory.getLogger(OKHttp.class);
  OkHttpClient client;

  public OKHttp() {
    client = new OkHttpClient();
  }

  public String get(String url) {
    Request request = new Request.Builder().url(url).get().build();
    try {
      Response response = client.newCall(request).execute();
      String content=null;
      if (response.isSuccessful()) {
        //请求成功 [200...300)
        content = response.body().string();
      }
      response.body().close();
      return content;
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  return null;
}

  public String post(String url, Map<String, String> parameter) {
    return null;
  }
}

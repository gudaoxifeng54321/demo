package com.ly.common.http;

import java.io.FileOutputStream;
import java.io.InputStream;

import com.ly.common.X;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class OkHttpUtil {


  public static String get() {
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder().url("http://dl.f.go2.cn/yiyiman/20160315180351498/F708&F708.zip?s=RFRF2qlL3VONzc_p0BviHQ&t=1475145462").get()
        .addHeader("referer", "").build();
    try {
      Response response = client.newCall(request).execute();
      if(!response.isSuccessful()){
        System.out.println(0);
      }
      System.out.println("request = " + request);
      Headers responseHeaders = response.headers();
      for (int i = 0; i < responseHeaders.size(); i++) {
        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
      }
      int x=response.code();
      System.out.println(x);
      InputStream is=response.body().byteStream();
      X.copyStream(is, new FileOutputStream("/Users/gaofeng/Desktop/aaa.zip"));
      System.out.println(1);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public static void main(String[] args) {
    get();
  }
}

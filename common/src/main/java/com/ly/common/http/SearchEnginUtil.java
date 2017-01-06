package com.ly.common.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public final class SearchEnginUtil {
  public static String post(String url, String content) {
    StringBuilder sb = new StringBuilder();
    try {
      HttpURLConnection httpURLConnection = getConnectionForPost(url);
      // 把请求参数写入请求中
      BufferedWriter br = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
      br.write(content);
      br.flush();
      br.close();
      sb = new StringBuilder();
      // httpURLConnection.connect();
      if (httpURLConnection.getResponseCode() == 200) {
        // 建立实际的连接
        // 获取所有响应头字段
        // Map<String, List<String>> map = httpURLConnection.getHeaderFields();
        // 遍历所有的响应头字段
        // for (String key : map.keySet()) {
        // System.out.println(key + "--->" + map.get(key));
        // }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
          sb.append(line).append("\n");
        }
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }

  public static String get(String url) {
    StringBuilder sb = new StringBuilder();
    try {
      HttpURLConnection httpURLConnection = getConnectionForGet(url);
      if (httpURLConnection.getResponseCode() == 200) {
        // 建立实际的连接
        httpURLConnection.connect();
        // 获取所有响应头字段
        // Map<String, List<String>> map = httpURLConnection.getHeaderFields();
        // 遍历所有的响应头字段
        // for (String key : map.keySet()) {
        // System.out.println(key + "--->" + map.get(key));
        // }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
          sb.append(line).append("\n");
        }
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }

  private static HttpURLConnection getConnectionForPost(String url) {
    HttpURLConnection connection = getConnectionForGet(url);
    try {
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
    } catch (ProtocolException e) {
      e.printStackTrace();
      return null;
    }

    return connection;
  }

  private static HttpURLConnection getConnectionForGet(String url) {
    HttpURLConnection connection = null;
    try {
      URL u = new URL(url);
      connection = (HttpURLConnection) (u.openConnection());
      connection.setRequestProperty("Accept-Charset", "utf-8");
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return connection;
  }
  
}

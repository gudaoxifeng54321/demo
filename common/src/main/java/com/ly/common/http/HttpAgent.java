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
import java.nio.charset.Charset;

/**
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source code file is protected by copyright law and international
 * treaties. Unauthorized distribution of source code files, programs, or portion of the package, may result in severe civil and criminal
 * penalties, and will be prosecuted to the maximum extent under the law.
 * 
 * HTTP 协议简单实现, 用作发送 GET/POST 请求.
 * 
 * @author gaofeng
 * @since 2015-11-13
 */
public class HttpAgent {
  /**
   * 发送一个POST请求
   * 
   * @param url
   * @param content
   * @return
   */
  public static void asynPost(String url, String content) {
    Thread httpThread = new Thread(new HttpThread("post", url, content));
    httpThread.start();
  }

  /**
   * 发送一个POST请求
   * 
   * @param url
   * @param content
   * @return
   */
  public static void asynGet(String url) {
    Thread httpThread = new Thread(new HttpThread("get", url));
    httpThread.start();
  }

  /**
   * 发送一个POST请求
   * 
   * @param url
   * @param content
   * @return
   */
  public static String post(String url, String content) {
    StringBuilder sb = new StringBuilder();
    try {
      HttpURLConnection httpURLConnection = getConnectionForPost(url);
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

  /**
   * 发送一个GET 请求
   * 
   * @param url
   * @return 返回请求的响应内容
   */
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
      } else {
        httpURLConnection.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
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
  /**
   * 发送一个GET 请求
   * 
   * @param url
   * @return 返回请求的响应内容
   */
  public static String get(String url,String charset) {
    StringBuilder sb = new StringBuilder();
    try {
      HttpURLConnection httpURLConnection = getConnectionForGet(url,charset);
      
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
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),charset));
        String line;
        while ((line = in.readLine()) != null) {
          sb.append(line).append("\n");
        }
      } else {
        httpURLConnection.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
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

  /**
   * 创建POST 的HTTP connection
   * 
   * @param url
   * @return
   */
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

  /**
   * 创建一个GET 的HTTP connection
   * 
   * @param url
   * @return
   */
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
  /**
   * 创建一个GET 的HTTP connection
   * 
   * @param url
   * @return
   */
  private static HttpURLConnection getConnectionForGet(String url,String charset) {
    HttpURLConnection connection = null;
    try {
      URL u = new URL(url);
      connection = (HttpURLConnection) (u.openConnection());
      connection.setRequestProperty("Accept-Charset", charset);
      connection.setRequestProperty("Charset", charset);
//      connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
      connection.setRequestProperty("Content-Type", "application/json;charset="+charset);
      connection.setRequestProperty("accept-language", "zh-CN");
//      connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return connection;
  }

  /**
   * 
   * @author lff
   * @param url
   *          访问地址 能够抛出异常的get方法
   * @throws IOException
   */

  public static String getCanThrowException(String url) throws IOException {
    StringBuilder sb = new StringBuilder();
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
    } else {
      httpURLConnection.connect();
      BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
      String line;
      while ((line = in.readLine()) != null) {
        sb.append(line).append("\n");
      }
    }

    return sb.toString();
  }

  public static class HttpThread implements Runnable {
    String type = "get";
    String url;
    String content;

    public HttpThread(String type, String url) {
      this.type = type;
      this.url = url;
    }

    public HttpThread(String type, String url, String content) {
      this.type = type;
      this.url = url;
      this.content = content;
    }

    @Override
    public void run() {
      if (type.equals("post")) {
        try {
          HttpURLConnection httpURLConnection = getConnectionForPost(url);
          BufferedWriter br = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
          br.write(content);
          br.flush();
          br.close();
        } catch (MalformedURLException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        try {
          HttpURLConnection httpURLConnection = getConnectionForGet(url);
          if (httpURLConnection.getResponseCode() == 200) {
            // 建立实际的连接
            httpURLConnection.connect();
          }
        } catch (MalformedURLException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

  }

  public static void main(String[] args) {
    // String
    // re=HttpAgent.get("http://7xkr0e.com2.z0.glb.qiniucdn.com/1/1013531/20150925/2015092509539271898431.jpg?imageView2/2/w/750/q/90|saveas/Z28ybW0tdGh1bWJuYWlsOjEvMTAxMzUzMS8yMDE1MDkyNS8yMDE1MDkyNTA5NTM5MjcxODk4NDMxXzc1MC5qcGc=/sign/UIgBw7sNUlZ1ANb0GpLeydDrY8T59cpRGTDut0c2:T-HmnA4I3Nc4FCuaAZgs77LZlWw=&v=15");
    try {
      HttpAgent.getCanThrowException("http://127.0.0.1:8080/jmx/register?url=127.0.0.1:18080&nodeId=userCenter_1");
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    // System.out.println(re);
  }
}

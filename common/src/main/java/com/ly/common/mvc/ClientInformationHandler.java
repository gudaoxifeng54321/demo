package com.ly.common.mvc;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source
 * code file is protected by copyright law and international treaties.
 * Unauthorized distribution of source code files, programs, or portion of the
 * package, may result in severe civil and criminal penalties, and will be
 * prosecuted to the maximum extent under the law.
 * <p/>
 * 客户端信息处理器, 用于将http header中的信息抓取到PageView中
 *
 * @author gaofeng
 * @since 2015-10-30
 */
public class ClientInformationHandler {

  /**
   * 加载request header 中附带的浏览器信息,存储到PageView 对象中
   *
   * @param request
   * @param box
   */
  public static void load(HttpServletRequest request, PageView pageView) {
    String ip = request.getHeader("X-Real-IP");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      //没有被反向代理转发过的 请求头
      ip = request.getRemoteAddr();
    }
    pageView.setIp(ip);
    pageView.setReferer(request.getHeader("referer"));
    String agent = request.getHeader("user-agent");
    pageView.setAgent(agent);
    pageView.setOs(getOS(agent));
    pageView.setBrowser(getBrowser(agent));
    pageView.setUrl(request.getRequestURL().toString());
  }

  /**
   * @param userAgent
   * @return 客户端操浏览器
   */
  public static String getBrowser(String userAgent) {
    if (null == userAgent || "".equals(userAgent.trim()))
      return "Unknow";
    //非主流 浏览器
    if (userAgent.indexOf("Opera") > 0)
      return "Opera";
    if (userAgent.indexOf("Firefox") > 0)
      return "Firefox";
    if (userAgent.indexOf("LBBROWSER") > 0)
      return "LieBao";
    if (userAgent.indexOf("MicroMessenger") > 0)
      return "wechat";
    if (userAgent.indexOf("MQQBrowser") > 0)
      return "MQQBrowser";
    if (userAgent.indexOf("QQ") > 0)
      return "QQ";

    if (userAgent.indexOf("UCBrowser") > 0)
      return "UC";
    if (userAgent.indexOf("baiduboxapp") > 0)
      return "baiduboxapp";
    if (userAgent.indexOf("SogouMobileBrowser") > 0)
      return "Sogou";
    if (userAgent.indexOf("SamsungBrowser") > 0)
      return "Samsung";

    //各种主流爬虫机器人
    if (userAgent.indexOf("DNSPod-Monitor") > 0)
      return "DNSPod-Monitor";
    if (userAgent.indexOf("Baiduspider") > 0)
      return "BaiduBot";
    if (userAgent.indexOf("Googlebot") > 0)
      return "GoogleBot";
    if (userAgent.indexOf("Bingbot") > 0)
      return "BingBot";
    if (userAgent.indexOf("YisouSpider") > 0)
      return "YisouBot";
    if (userAgent.indexOf("360spider") > 0)
      return "360Bot";
    if (userAgent.indexOf("SinaWeiboBot") > 0)
      return "SinaWeiboBot";
    if (userAgent.indexOf("YandexBot") > 0)
      return "YandexBot";
    if (userAgent.indexOf("Yahoo! Slurp") > 0)
      return "YahooBot";
    if (userAgent.indexOf("Sogou web spider") > 0)
      return "SogouBot";
    if (userAgent.indexOf("smtbot") > 0)
      return "smtBot";
    if (userAgent.indexOf("JikeSpider") > 0)
      return "JikeBot";
    if (userAgent.indexOf("GrapeshotCrawler") > 0)
      return "GrapeshotBot";
    if (userAgent.indexOf("Dataprovider") > 0)
      return "DataproviderBot";
    if (userAgent.indexOf("MJ12bot") > 0)
      return "MJ12Bot";
    if (userAgent.indexOf("360JK qiyunce") > 0)
      return "360qiyunce";
    if (userAgent.indexOf("AliApp") > 0)
      return "AliApp";
    if (userAgent.indexOf("NSPlayer") > 0)
      return "WindowsMediaPlayer";

    //各种自定义爬虫   极有可能是爬数据的脚本.
    if (userAgent.indexOf("Python-urllib") > 0)
      return "Program_Python-urllib";
    if (userAgent.indexOf("curl") > 0)
      return "Program_curl";
    if (userAgent.indexOf("Wget") > 0)
      return "Program_Wget";
    if (userAgent.indexOf("okhttp") > 0)
      return "Program_okhttp";
    if (userAgent.indexOf("Java") > 0)
      return "Program_Java";
    if (userAgent.indexOf("Jakarta") > 0)
      return "Program_Jakarta";
    if (userAgent.indexOf("Apache-HttpClient") > 0)
      return "Program_Apache-HttpClient";
    if (userAgent.indexOf("python-requests") > 0)
      return "Program_python-requests";
    if (userAgent.indexOf("Scrapy") > 0)
      return "Program_Scrapy";
    if (userAgent.indexOf("larbin") > 0)
      return "Program_larbin";
    if (userAgent.indexOf("nutch") > 0)
      return "Program_nutch";
    if (userAgent.indexOf("ApacheBench") > 0)
      return "Program_ApacheBench";

    //主流浏览器
    if (userAgent.indexOf("Edge") > 0)
      return "Edge";
    if (userAgent.indexOf("Chrome") > 0)
      return "Chrome";
    if (userAgent.indexOf("Safari") > 0)
      return "Safari";
    if (userAgent.indexOf("iPhone") > 0)
      return "Safari";
    if (userAgent.indexOf("iPad") > 0)
      return "Safari";
    if (userAgent.indexOf("Android") > 0)
      return "Android";

    if (userAgent.indexOf("MSIE") > 0)
      return "IE";
    if (userAgent.indexOf("Trident") > 0)
      return "IE";
    return "Unknow";
  }

  /**
   * @param userAgent
   * @return 客户端操作系统
   */
  public static String getOS(String userAgent) {
    if (null == userAgent || "".equals(userAgent.trim()))
      return "Unknow";
    if (userAgent.indexOf("Windows") > 0)
      return "Windows";
    if (userAgent.indexOf("Android") > 0)
      return "Android";
    if (userAgent.indexOf("iPhone") > 0)
      return "iPhone";
    if (userAgent.indexOf("Macintosh") > 0)
      return "OS X";
    if (userAgent.indexOf("iPad") > 0)
      return "iPad";
    if (userAgent.indexOf("Linux") > 0)
      return "Linux";
    return "Unknow";
  }
}

package com.ly.common.img;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ly.common.Constant;
import com.ly.common.X;
import com.ly.common.http.HttpAgent;

/**
 * 
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source code file is protected by copyright law and international
 * treaties. Unauthorized distribution of source code files, programs, or portion of the package, may result in severe civil and criminal
 * penalties, and will be prosecuted to the maximum extent under the law.
 * 
 * go2相对地址和绝对地址互转
 * 
 * @author gzh
 * @since 20151230
 */

public class ImgAddressConvert implements Constant {

  /**
   * 老图片位于http://image.go2.cn
   * 
   * 根据相对地址转成go2图片地址
   */
  public static String relativeURLToGo2(String inStr) {
    // 匹配
    String imgPatt = "src=\"/+(\\d+/[\\w!]+.(jpg|png|gif|jpeg))\"";
    // 忽略大小写
    Pattern pattern = Pattern.compile(imgPatt, Pattern.CASE_INSENSITIVE);
    Matcher m = pattern.matcher(inStr);
    StringBuffer stringBuffer = new StringBuffer();
    while (m.find()) {
      m.appendReplacement(stringBuffer, "src=\"http://image.go2.cn/" + m.group(1) + "\"");
    }
    return stringBuffer.toString();
    // http://image.go2.cn/13556/2015100623003990678591.jpg
  }

  /**
   * 产品详情页地址转换
   * 
   * 替换jpg/png等图片地址
   * 
   * 修改尺寸
   * 
   * @param inString
   * @return
   */
  public static String convertImgSize(String inString) {
    // jpg,png替换图片尺寸
    String imgPatt = "http://[image|img]+.go2.cn/+(\\d+)/([\\w!]+).(jpg|png|jpeg)";
    Pattern pattern = Pattern.compile(imgPatt, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(inString);

    // 当前时间
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMDD");
    Long now = System.currentTimeMillis();
    String today = dateFormat.format(now);

    StringBuffer sb = new StringBuffer();
    while (matcher.find()) {
      matcher.appendReplacement(sb, STATIC_BIG_IMAGE_URL_PREFIX + "/imgs/in/0/" + matcher.group(1) + "/0/" + today + "/" + matcher.group(2)
          + "_750X750." + matcher.group(3));
    }
    return sb.toString();
  }

  /**
   * 1b端产品编辑页面
   * 
   * 相对地址转绝对地址
   */

  public static String editedProductAddrConvert(String inStr) {
    String p = "src=\"/+(\\d*+/[\\w!]+.jpg|png|gif|jpeg)\"";
    Pattern pattern = Pattern.compile(p, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(inStr);
    StringBuffer sb = new StringBuffer();
    while (matcher.find()) {
      matcher.appendReplacement(sb, "src=\"http://content.ximgs.net/" + matcher.group(1) + "\"");
    }
    return sb.toString();
  }

  /**
   * 替换为图片网络地址(临时使用,待七牛云切换完成后切换代码)
   * 
   * @param content
   *          需要替换的字符串
   * @param sellerId
   *          卖家用户id
   * @param type
   *          图片资源类型
   * @param taobao
   * @param size
   *          图片尺寸
   * @param flag
   *          替换 or 返回图片地址列表
   * @return
   */
  public static String commonConvertImgUrl(String content, String sellerId, String type, String taobao, String size, boolean flag) {
    List<String> urlList = new ArrayList<String>();
    List<String> absoluteUrlList = new ArrayList<String>();
    String imgHost = "";
    // 替换为网络地址
    if (null != taobao && !taobao.equals("") && sellerId != null)
      imgHost = X.STATIC_TAOBAO_IMAGE_URL_PREFIX + "/imgs/in/" + sellerId;
    else
      imgHost = X.STATIC_IMAGE_URL_PREFIX + X.SLASH;

    if (flag) {

      if (type.equals("thumb")) {
        Matcher matcher = Pattern.compile("/+(\\d+/[\\w!]+.(jpg|png|gif|jpeg))", Pattern.CASE_INSENSITIVE).matcher(content);

        StringBuilder url = new StringBuilder("http://p.go2.cn:8080/v2/checker.php?file=");
        while (matcher.find()) {
          urlList.add(matcher.group());
          url.append(matcher.group()).append(",");
        }

        String[] arr = HttpAgent.get(url.toString()).split(",");

        String str = "";
        for (int i = 0; i < urlList.size(); i++) {
          if (arr[i].equals("1")) {
            if (sellerId != null) {
              Matcher m = Pattern.compile("/+(\\d+)/([\\w!]+).(jpg|png|jpeg)", Pattern.CASE_INSENSITIVE).matcher(urlList.get(i));
              StringBuffer bf = new StringBuffer();
              while (m.find()) {
                m.appendReplacement(bf,
                    imgHost + X.SLASH + m.group(1) + "/0/" + new SimpleDateFormat("yyyyMMDD").format(System.currentTimeMillis()) + X.SLASH
                        + m.group(2) + X.UNDER_LINE + size + X.DOT + m.group(3));
                absoluteUrlList.add(bf.toString());
                str += bf.toString() + "||";
              }
            } else {

            }
          }
        }

        return str;

        // 返回图片地址列表
      } else {
        return null;
      }
    } else {
      Matcher m = Pattern.compile("/+(\\d+)/([\\w!]+).(jpg|png|jpeg)", Pattern.CASE_INSENSITIVE).matcher(content);
      StringBuffer sb = new StringBuffer();
      while (m.find()) {
        String url = imgHost + X.SLASH + m.group(1) + "/0/" + new SimpleDateFormat("yyyyMMDD").format(System.currentTimeMillis()) + X.SLASH
            + m.group(2) + X.UNDER_LINE + size + X.DOT + m.group(3);
        m.appendReplacement(sb, url);
      }
      return sb.toString();
    }

  }

  /**
   * 实力制造 产品列表相对地址转绝对地址
   * 
   * @param 图片相对地址
   * @param 图片显示大小
   * @return
   */
  public static String imgAddressRelToAbs(String imgAddress, String imgSize) {
    Pattern partern = Pattern.compile("^(\\/\\d+)(\\d)\\/(main\\!)?(\\d{8,8})(\\d+)\\.(\\w+)$");
    Matcher macher = partern.matcher(imgAddress);
    String newUrl = "";
    try {
      if (macher.find()) {
        newUrl = X.IMAGE_URL_PREFIX_GO2 + "/" + macher.group(2) + macher.group(1) + macher.group(2) + "/" + macher.group(4) + "/";
        if (macher.group(3) != null) {
          newUrl += "main_";
        }
        newUrl += macher.group(4) + macher.group(5) + "_" + imgSize + "." + macher.group(6).toLowerCase();
      }
      return newUrl;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  /***
   * 产品图片相对地址转绝对地址
   * @param imgAddress 图片地址
   * @param imgSize 图片显示大小
   * @param outer商品来源标识   0(go2)  1(bag8) 2(2mm) 3(3e3e) 
   */
  public static String getProductImg(String imgAddress, String imgSize, Integer outer) {
    String imgURL = null;
    if (outer ==null||outer == 0)
      imgURL = IMAGE_URL_PREFIX_GO2;
    if (outer == 1)
      imgURL = IMAGE_URL_PREFIX_BAG8;
    if (outer == 2)
      imgURL = IMAGE_URL_PREFIX_2MM;
    if (outer == 3)
      imgURL = IMAGE_URL_PREFIX_3E3E;
    try {
      String[] array = imgAddress.split("/");
      if (outer == 1 || outer == 3) {// 3e3e/bag8处理
        imgAddress = "/" + array[array.length - 3] + "/" + array[array.length - 1];
      }else if(outer == 2){ //2mm图片地址处理
        imgAddress = "/" + array[array.length - 2] + "/" + array[array.length - 1];
      }
      Pattern partern = Pattern.compile("^(\\/\\d+)(\\d)\\/(main\\!)?(\\d{8,8})(\\d+)\\.(\\w+)$");
      Matcher macher = partern.matcher(imgAddress);
      String newUrl = "";
      if (macher.find()) {
        newUrl = imgURL + "/" + macher.group(2) + macher.group(1) + macher.group(2) + "/" + macher.group(4) + "/";
        if (macher.group(3) != null) {
          newUrl += "main_";
        }
        newUrl += macher.group(4) + macher.group(5) + "_" + imgSize + "." + macher.group(6).toLowerCase();
      }
      return newUrl;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static List<String> productImgs(String img) {
    // 去掉产品详情描述的其他信息，只保留图片信息
    Pattern pattern = Pattern.compile("<img src=\"(/\\d+/\\w+.\\w+)\"|//s/>");
    Matcher matcher = pattern.matcher(img);
    List<String> productImgs = new ArrayList<>();
    while (matcher.find()) {
      // 转go2地址
      productImgs.add(matcher.group(1).toLowerCase());
    }
    return productImgs;
  }
  
  public static List<String> productImgs2(String img) {
    // 去掉产品详情描述的其他信息，只保留图片信息
    Pattern pattern = Pattern.compile("<img src=\"(http://[a-zA-Z0-9\\.\\-]+/\\d/\\d+/\\d+/\\d+_\\d+.\\w+)\"\\s/>");
//  Pattern pattern = Pattern.compile("<img src=\"(.*)(http://.*/)(\\d)/(\\d+/)(\\d+)/(\\d+)(_\\d+)(.)(\\w+\")|//s/>");
    
    
    Matcher matcher = pattern.matcher(img);
    List<String> productImgs = new ArrayList<>();
    while (matcher.find()) {
      // 转go2地址
      productImgs.add(matcher.group(1).toLowerCase());
    }
    return productImgs;
  }
  public static List<String> productImgs3(String img) {
	  // 去掉产品详情描述的其他信息，只保留图片信息
	  Pattern pattern = Pattern.compile("<img src=\"(http://[a-zA-Z0-9\\.\\-]+/\\d/\\d+/\\d+/\\d+_\\d+.\\w+)\"\\salt=\"\"\\s/>");
//  Pattern pattern = Pattern.compile("<img src=\"(.*)(http://.*/)(\\d)/(\\d+/)(\\d+)/(\\d+)(_\\d+)(.)(\\w+\")|//s/>");
	  
	  
	  Matcher matcher = pattern.matcher(img);
	  List<String> productImgs = new ArrayList<>();
	  while (matcher.find()) {
		  // 转go2地址
		  productImgs.add(matcher.group(1).toLowerCase());
	  }
	  return productImgs;
  }
  

  public static String getTagImage(String code) {
    if (code.equals("TBDZH")) {
      // 淘宝店找货
      return "taobao.png";
    } else if (code.equals("TMDZH")) {
      // 天猫店找货
      return "tmall.png";
    } else if (code.equals("ALBBZH")) {
      // 阿里巴巴找货
      return "ali.png";
    } else if (code.equals("WDZH")) {
      // 微店找货
      return "vdian.png";
    } else if (code.equals("MLSZH")) {
      // 美丽说找货
      return "meilishuo.png";
    } else if (code.equals("MGJZH")) {
      // 蘑菇街找货
      return "mogujie.png";
    } else if (code.equals("STDZH")) {
      // 实体店找货
      return "shiti.png";
    } else if (code.equals("DTZH")) {
      // 地摊找货
      return "ditan.png";
    } else if (code.equals("QT")) {
      // 其他
      return "";
    } else {
      return null;
    }
  }
  
  
  public static String checkImgUrl(String relUrl,String defaultUrl){
	  HttpURLConnection connection = null;
	    try {
	      URL u = new URL(relUrl);
	      connection = (HttpURLConnection) (u.openConnection());
	      connection.setRequestProperty("Accept-Charset", "utf-8");
	      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	      if (connection.getResponseCode() == 200) {
	    	  return relUrl+"?"+(new Date()).getTime();
	      }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return defaultUrl;
	    }
	    return defaultUrl;
      
  }
}

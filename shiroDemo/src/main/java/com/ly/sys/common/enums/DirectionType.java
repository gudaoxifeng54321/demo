package com.ly.sys.common.enums;
/**
 * <p>Description: 
 * <p>User: mtwu
 * <p>Date: 2015-12-18
 * <p>Version: 1.0
 */
public enum DirectionType {
  //1、华东地区（包括山东、江苏、安徽、浙江、福建、上海）； 
  //2、华南地区（包括广东、广西、海南）； 
  //3、华中地区（包括湖北、湖南、河南、江西）； 
  //4、华北地区（包括北京、天津、河北、山西、内蒙古）； 
  //5、西北地区（包括宁夏、新疆、青海、陕西、甘肃）； 
  //6、西南地区（包括四川、云南、贵州、西藏、重庆）； 
  //7、东北地区（包括辽宁、吉林、黑龙江）； 
  //8、台港澳地区（包括台湾、香港、澳门）。 
  //9、钓鱼岛

  hd("华东地区"),hn("华南地区"),hz("华中地区"),hb("华北地区"),xb("西北地区"),xn("西南地区"),db("东北地区"),tga("台港澳地区"),dyd("钓鱼岛");
  
  private final String info;

  private DirectionType(String info) {
      this.info = info;
  }

  public String getInfo() {
      return info;
  }
}

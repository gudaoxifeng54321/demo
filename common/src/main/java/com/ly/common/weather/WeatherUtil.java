package com.ly.common.weather;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ly.common.http.HttpAgent;

/**
 * <p>Description: 
 * <p>User: mtwu
 * <p>Date: 2016-8-26
 * <p>Version: 1.0
 */
public class WeatherUtil {
  private final static String encryptKey = "112ei4tvmlqnm5qg";
  public final static String CHENGDU = "chengdu";
  /**
   * 获取当天天气情况
   * @param city （城市拼音/英文名）
   * @return
   */
  public static String getWearthToday(String city){
    StringBuffer result = new StringBuffer();
    String url = "https://api.thinkpage.cn/v3/weather/daily.json?key={encryptKey}&location={city}&language=zh-Hans&unit=c&start=0&days=1";
    try {
      url = url.replace("{city}", city).replace("{encryptKey}", encryptKey);
      String text = HttpAgent.get(url);
      JSONObject json = JSONObject.parseObject(text);
      JSONArray resuts = json.getJSONArray("results");
      JSONObject obj = (JSONObject)resuts.get(0);
      String location = obj.getJSONObject("location").getString("name");
      JSONObject daily = obj.getJSONArray("daily").getJSONObject(0);
      result.append("今天[").append(location).append("]：温度");
      String high = daily.getString("high");
      String low = daily.getString("low");
      String textDay = daily.getString("text_day");
      String textNight = daily.getString("text_night");
      String windDirection = daily.getString("wind_direction");
      String windScale = daily.getString("wind_scale");
      result.append(low).append("°C~").append(high).append("°C。白天：").append(textDay);
      result.append("，夜间：").append(textNight).append("。").append(windDirection);
      result.append("风").append(windScale).append("级。");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result.toString();
  }
  
  public static void main(String args[]){
    String result = WeatherUtil.getWearthToday("chengdu");
    System.out.println(result+result.length());
  }
}

package com.ly.core.util.juheApi;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.ly.common.http.HttpAgent;
import com.ly.common.json.Json;
import com.ly.common.time.TimeUtil;


public class JokeTest {
	public final static String jokeUrl = "http://japi.juhe.cn/joke/content/list.from";
	public final static String jokeKey = "a8ebe353e6bdc2b26c53613ea0b18255";
	
	 @BeforeClass
	  public static void setUpBeforeClass() throws Exception {
	    //ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:IOC.xml");
	  }
	 
	 
	 /**
	  * 	名称	类型	必填	说明
	  	sort	string	是	类型，desc:指定时间之前发布的，asc:指定时间之后发布的
	  	page	int	否	当前页数,默认1
	  	pagesize	int	否	每次返回条数,默认1,最大20
	  	time	string	是	时间戳（10位），如：1418816972
	  	key	string	是	您申请的key
	  * @author go2
	  *
	  */
	 @Test
	 public void jokeTest(){
		 String t = "2017-01-22 00:00:00";
		 Date d = TimeUtil.parseStringToDate(t);
		 long time = d.getTime();
		 time = time/1000;
		 StringBuilder sb = new StringBuilder(jokeUrl);
		 sb.append("?").append("key=").append(jokeKey);
		 sb.append("&").append("sort=asc");
		 sb.append("&").append("page=2");
		 sb.append("&").append("pagesize=20");
		 sb.append("&").append("time=").append(time);
		 String rt = HttpAgent.get(sb.toString());
		 JSONObject jsonObject = JSONObject.parseObject(rt);
		 System.out.println(Json.toJson(jsonObject));
	 }
	 

}

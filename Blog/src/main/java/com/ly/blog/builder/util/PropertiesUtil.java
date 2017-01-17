package com.ly.blog.builder.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @Description:  
 * @User: mtwu
 * @Date: 2012-12-9 16:44:35
 * @Version: 1.0
 */

public class PropertiesUtil {
	public static Properties getDatebaseProperties() {
		InputStream in = PropertiesUtil.class.getResourceAsStream("/builder/jdbc/builder-database.properties");
		Properties varp = new Properties();
		try {
			varp.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return varp;
	}
	
	public static Properties getConfigProperties() {
		InputStream in = PropertiesUtil.class.getResourceAsStream("/builder/jdbc/builder-dfaultPath.properties");
		Properties varp = new Properties();
		try {
			varp.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return varp;
	}
	
	public static String getProperty(Properties varp,String name,String encode){
		try {
			return new String(varp.getProperty(name).getBytes("GBK"),encode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getProperty(Properties varp,String name){
		try {
			return new String(varp.getProperty(name));
		} catch ( Exception e ) {
			e.printStackTrace();
			return null;
		}
	}
}
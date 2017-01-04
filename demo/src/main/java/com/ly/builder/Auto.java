package com.ly.builder;

import com.ly.builder.core.IBuildeController;

/**
 * @Description:  创建文件
 * @User: mtwu
 * @Date: 2012-12-9 16:44:35
 * @Version: 1.0
 */
public class Auto {
	public static void main(String[] args) {
		IBuildeController buildeController = BuilderFactory.getinstance().getBuildeController();
		String xmlFilePath = Auto.class.getResource("/builder/config/builder-config.xml").getPath();
		buildeController.defaultBuild(xmlFilePath);
	}
}

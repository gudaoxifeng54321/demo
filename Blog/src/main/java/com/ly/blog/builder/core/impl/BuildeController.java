package com.ly.blog.builder.core.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ly.blog.builder.BuilderFactory;
import com.ly.blog.builder.core.IBuildeController;
import com.ly.blog.builder.core.IBuilder;
import com.ly.blog.builder.model.Maker;
import com.ly.blog.builder.model.TableColumns;
import com.ly.blog.builder.util.DBUtil;
import com.ly.blog.builder.util.VelocityUtil;
import com.ly.blog.builder.util.XmlUtil;


/**
 * @Description:  模块生成控制器接口实现
 * @User: mtwu
 * @Date: 2012-12-9 16:44:35
 * @Version: 1.0
 */
public class BuildeController implements IBuildeController {

	public void defaultBuild(String xmlFilePath) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("---------------------------------------------------------------------\n\t\t")
    		.append("~!~ building, please wait!...")
    		.append("\n---------------------------------------------------------------------\n");
			System.out.println(sb.toString());
			
			System.out.println("xmlFilePath:"+xmlFilePath);
			XmlUtil.load(xmlFilePath);
			ArrayList<Maker> makers = XmlUtil.getContents();
            int len = makers.size();
            if(makers.size() == 0){
            	System.out.println("~!~没有找到可用的Maker, 请确认builder-config.xml配置文件");
            }
            sb = new StringBuilder();
            for ( int i = 0; i < len; i++ ) {
            	System.out.print("---------------------------------------------------------------------\n\t\t");
            	System.out.println("~!~ process "+makers.get(i).getTableName());
            	Maker maker = makers.get(i);
            	buildeDefaultFiles(maker);
            	System.out.print("\n\t\t");
            	System.out.print("~!~ build "+makers.get(i).getTableName()+" success!");
            	System.out.print("\n---------------------------------------------------------------------");
            	sb = new StringBuilder();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建默认文件
	 */
	private  void buildeDefaultFiles(Maker maker) {
	    //获取数据库信息
		List<TableColumns> list = DBUtil.getDbTableInfo(maker.getTableName(), maker.getNeedSchema());
		VelocityUtil agt = new VelocityUtil();  
        try {
			agt.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		IBuilder builder = BuilderFactory.getinstance().getBuilder(maker, list, agt);
		//创建JavaBean类
		builder.buildeJavaBean();
		//创建QueryJavaBean类
		//builder.buildeQueryJavaBean();
		
		//创建DAO接口类
		builder.buildeRepository();
		//创建DAO接口实现类
		//builder.buildeDAOImpl();
		
		//创建Service接口类
		//builder.buildeIService();
		//创建Service接口实现类
		builder.buildeServiceImpl();
		//创建IBatis XML文件
		builder.buildeIBatis();
		//创建Struts Action 文件
		builder.buildeController();
		//创建Struts Form 文件
		//builder.buildeForm();
		//根据XML中的frames标签内容配置框架中的配置文件
//		builder.configFrames();
	}
}
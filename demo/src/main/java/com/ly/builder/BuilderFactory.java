package com.ly.builder;

import java.util.List;

import com.ly.builder.core.IBuildeController;
import com.ly.builder.core.IBuilder;
import com.ly.builder.core.impl.BuildeController;
import com.ly.builder.core.impl.Builder;
import com.ly.builder.model.Maker;
import com.ly.builder.model.TableColumns;
import com.ly.builder.util.VelocityUtil;

public class BuilderFactory {
	
	private static BuilderFactory instance = new BuilderFactory();

	/**
	 * 单例 构造方法
	 */	
	private BuilderFactory() {
	}

	/**
	 * 单例 构造方法
	 * @return
	 */
	public static BuilderFactory getinstance() {
		return instance;
	}
	
	public IBuildeController getBuildeController(){
		return new BuildeController();
	}
	
	public IBuilder getBuilder(Maker maker,List<TableColumns> list,VelocityUtil agt){
		return new Builder(maker,list,agt);
	}
}

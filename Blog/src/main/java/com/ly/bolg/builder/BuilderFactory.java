package com.ly.bolg.builder;

import java.util.List;

import com.ly.bolg.builder.core.IBuildeController;
import com.ly.bolg.builder.core.IBuilder;
import com.ly.bolg.builder.core.impl.BuildeController;
import com.ly.bolg.builder.core.impl.Builder;
import com.ly.bolg.builder.model.Maker;
import com.ly.bolg.builder.model.TableColumns;
import com.ly.bolg.builder.util.VelocityUtil;

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

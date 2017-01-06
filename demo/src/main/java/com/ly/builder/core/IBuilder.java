package com.ly.builder.core;

/**
 * @Description:  
 * @User: mtwu
 * @Date: 2012-12-9 16:44:35
 * @Version: 1.0
 */
public interface IBuilder {

	public void buildeJavaBean();
	
	public void buildeQueryJavaBean();
	
	public void buildeRepository();
	
	//public void buildeDAOImpl();
	
	public void buildeIBatis();
	
	public void buildeIService();
	
	public void buildeServiceImpl();
	
	public void buildeController();
	
	//public void buildeForm();
	
	public void configFrames();
}

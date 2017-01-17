package com.ly.blog.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;





/**
 * 设置通用数据的Interceptor
 * <p/>
 * 使用Filter时 文件上传时 getParameter时为null 所以改成Interceptor
 * <p/>
 * 1、ctx---->request.contextPath 2、currentURL---->当前地址
 * <p>
 * User: mtwu
 * <p>
 * Date: 13-1-22 下午4:35
 * <p>
 * Version: 1.0
 */
public class SetCommonDataInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		return super.preHandle(request, response, handler);
	}
}
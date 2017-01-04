
package com.ly.sys.shiro.session.mgt;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.OnlineSession;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.web.session.mgt.WebSessionContext;

import com.ly.common.http.IpUtils;
import com.ly.sys.core.user.vo.OperatorOnline;

/**
 * 创建自定义的session，
 * 添加一些自定义的数据
 * 如 用户登录到的系统ip
 * 用户状态（在线 隐身 强制退出）
 * 等 比如当前所在系统等
 * <p>User: mtwu
 * <p>Date: 13-3-20 下午2:33
 * <p>Version: 1.0
 */
public class OnlineSessionFactory implements SessionFactory {

    @Override
    public Session createSession(SessionContext initData) {
        OnlineSession session = new OnlineSession();
        if (initData != null && initData instanceof WebSessionContext) {
            WebSessionContext sessionContext = (WebSessionContext) initData;
            HttpServletRequest request = (HttpServletRequest) sessionContext.getServletRequest();
            if (request != null) {
            	String host=IpUtils.getIpAddr(request);
            	if("0:0:0:0:0:0:0:1".equals(host)){
            		host="127.0.0.1";
            	}
            	String localHost=request.getLocalAddr();
            	if("0:0:0:0:0:0:0:1".equals(localHost)){
            		localHost="127.0.0.1";
            	}
                session.setHost(host);
                session.setUserAgent(request.getHeader("User-Agent"));
                session.setSystemHost( localHost+ ":" + request.getLocalPort());
            }
        }
        return session;
    }

    public Session createSession(OperatorOnline userOnline) {
        return userOnline.getSession();
    }
}

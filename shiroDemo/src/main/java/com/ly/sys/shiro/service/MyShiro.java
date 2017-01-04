package com.ly.sys.shiro.service;


import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.ly.sys.core.auth.service.AuthService;
import com.ly.sys.core.user.exception.UserBlockedException;
import com.ly.sys.core.user.exception.UserException;
import com.ly.sys.core.user.exception.UserNotExistsException;
import com.ly.sys.core.user.exception.UserPasswordNotMatchException;
import com.ly.sys.core.user.exception.UserPasswordRetryLimitExceedException;
import com.ly.sys.core.user.service.OperatorService;
import com.ly.sys.core.user.vo.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyShiro extends AuthorizingRealm{
	private static final Logger log = LoggerFactory.getLogger("es-error");
	
	@Autowired
    private OperatorService operatorService;
    
    @Autowired
    private AuthService authService;
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		 String username = (String) principals.getPrimaryPrincipal();
	        Operator operator = operatorService.findByUsername(username);

	        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
	        authorizationInfo.setRoles(authService.findStringRoles(operator));
	        authorizationInfo.setStringPermissions(authService.findStringPermissions(operator));

	        return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername().trim();
        String password = "";
        if (upToken.getPassword() != null) {
            password = new String(upToken.getPassword());
        }

        Operator operator = null;
        try {
          operator = operatorService.login(username, password);
        } catch (UserNotExistsException e) {
            throw new UnknownAccountException(e.getMessage(), e);
        } catch (UserPasswordNotMatchException e) {
            throw new AuthenticationException(e.getMessage(), e);
        } catch (UserPasswordRetryLimitExceedException e) {
            throw new ExcessiveAttemptsException(e.getMessage(), e);
        } catch (UserBlockedException e) {
            throw new LockedAccountException(e.getMessage(), e);
        } catch (Exception e) {
            log.error("login error", e);
            throw new AuthenticationException(new UserException("user.unknown.error", null));
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(operator.getUsername(), password.toCharArray(), getName());
        return info;
	}

}

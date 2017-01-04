package com.ly.sys.core.user.exception;

/**
 * <p>User: mtwu
 * <p>Date: 13-3-11 下午8:29
 * <p>Version: 1.0
 */
public class UserPasswordNotMatchException extends UserException {

    public UserPasswordNotMatchException() {
        super("user.password.not.match", null);
    }
}

package com.ly.sys.core.user.exception;

import com.ly.sys.common.exception.BaseException;


/**
 * <p>User: mtwu
 * <p>Date: 13-3-11 下午8:19
 * <p>Version: 1.0
 */
public class UserException extends BaseException {

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }

}

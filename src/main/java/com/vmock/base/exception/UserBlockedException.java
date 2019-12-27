package com.vmock.base.exception;

/**
 * 用户锁定异常类
 *
 * @author mock
 */
public class UserBlockedException extends UserException {


    public UserBlockedException() {
        super("用户已锁定，请联系管理员");
    }
}

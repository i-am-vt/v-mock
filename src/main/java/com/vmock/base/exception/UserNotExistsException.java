package com.vmock.base.exception;

/**
 * 用户不存在异常类
 *
 * @author mock
 */
public class UserNotExistsException extends UserException {


    public UserNotExistsException() {
        super("用户不存在");
    }
}

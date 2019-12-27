package com.vmock.base.exception;

/**
 * 用户密码不正确或不符合规范异常类
 *
 * @author mock
 */
public class UserPasswordNotMatchException extends UserException {


    public UserPasswordNotMatchException() {
        super("密码错误");
    }
}

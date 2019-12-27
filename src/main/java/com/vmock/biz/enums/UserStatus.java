package com.vmock.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态
 *
 * @author mock
 */
@Getter
@AllArgsConstructor
public enum UserStatus {

    /**
     * 正常
     */
    OK(0, "正常"),

    /**
     * 停用
     */
    DISABLE(1, "停用");

    private final Integer code;

    private final String info;

}

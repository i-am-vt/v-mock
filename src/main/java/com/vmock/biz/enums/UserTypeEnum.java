package com.vmock.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举
 *
 * @author vt
 * @since 2019年11月28日
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    /**
     * 普通用户
     */
    GENERAL(0),

    /**
     * admin
     */
    ADMIN(1);

    /**
     * 类型值
     */
    private Integer code;

}

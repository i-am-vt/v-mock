package com.vmock.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单展示
 *
 * @author vt
 * @since 2019年11月28日
 */
@AllArgsConstructor
public enum UserDisplayEnum {

    /**
     * 普通用户
     */
    USER_DISPLAY(1);

    /**
     * 类型值
     */
    @Getter
    private final Integer code;
}

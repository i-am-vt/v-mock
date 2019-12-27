package com.vmock.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * response 启用状态
 *
 * @author vt
 * @since 2019年11月26日
 */
@Getter
@AllArgsConstructor
public enum MainEnum {

    /* 启用*/
    ENABLED(1),

    /* 停用*/
    DISABLED(0);


    private final Integer code;

}
package com.vmock.base.constant;

import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Shiro通用常量
 *
 * @author mock
 */
public final class LoginConst {

    /**
     * 登录记录缓存
     */
    public static final String TIME_OUT;

    /**
     * 系统活跃用户缓存
     */
    public static final String SYS_USER_CACHE = "sys-userCache";

    static {
        // TIME_OUT 字段赋值
        Map<String, String> resultMap = new HashMap<>(2);
        resultMap.put("code", "1");
        resultMap.put("msg", "未登录或登录超时。请重新登录");
        TIME_OUT = JSONUtil.toJsonStr(resultMap);
    }


}
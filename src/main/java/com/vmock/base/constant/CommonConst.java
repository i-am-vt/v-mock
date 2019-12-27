package com.vmock.base.constant;

/**
 * 通用常量信息
 *
 * @author mock
 */
public final class CommonConst {

    /**
     * 主业务url的content path
     */
    public static final String RESTFUL_PATH = "/vmock";

    /**
     * 无效ID 使用装箱类型
     */
    public static final Long INVAILD_ID = Long.valueOf(-1L);

    /**
     * path 传参占位符
     */
    public static final String PATH_PLACEHOLDER = "{path}";

    /**
     * sql limit 1
     */
    public static final String LIMIT_ONE = "limit 1";

    /**
     * 管理员ID
     */
    public static final Long ADMIN_ID = 1L;


    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 首页
     */
    public static final String INDEX_URL = "/index";

}

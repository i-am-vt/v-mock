package com.vmock.base.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import static cn.hutool.core.util.StrUtil.EMPTY;

/**
 * 分页数据
 *
 * @author mock
 */
@Data
public class PageVo {
    /**
     * 当前记录起始索引
     */
    private Integer pageNum;
    /**
     * 每页显示记录数
     */
    private Integer pageSize;
    /**
     * 排序列
     */
    private String orderByColumn;
    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    private String isAsc;

    public String getOrderBy() {
        if (StrUtil.isBlank(orderByColumn)) {
            return EMPTY;
        }
        return StrUtil.toUnderlineCase(orderByColumn).concat(StrUtil.SPACE).concat(isAsc);
    }
}

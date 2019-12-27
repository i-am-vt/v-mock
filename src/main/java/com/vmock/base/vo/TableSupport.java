package com.vmock.base.vo;

import com.vmock.base.constant.CommonConst;
import com.vmock.base.utils.ContextUtils;

/**
 * 表格数据处理
 *
 * @author mock
 */
public class TableSupport {
    /**
     * 封装分页对象
     */
    public static PageVo getPage() {
        PageVo pageVo = new PageVo();
        pageVo.setPageNum(ContextUtils.getParameterToInt(CommonConst.PAGE_NUM));
        pageVo.setPageSize(ContextUtils.getParameterToInt(CommonConst.PAGE_SIZE));
        pageVo.setOrderByColumn(ContextUtils.getParameter(CommonConst.ORDER_BY_COLUMN));
        pageVo.setIsAsc(ContextUtils.getParameter(CommonConst.IS_ASC));
        return pageVo;
    }
}

package com.vmock.base.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author mock
 */
@Data
public class TableDataVo<T> implements Serializable {


    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> rows;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息内容
     */
    private int msg;

}
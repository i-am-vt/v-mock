package com.vmock.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.vmock.base.annotation.Excel;
import com.vmock.base.annotation.Excel.ColumnType;
import lombok.Data;

/**
 * 操作日志记录表 oper_log
 *
 * @author mock
 */
@Data
@TableName("mock_log")
public class MockLog extends BaseEntity<MockLog> {

    /**
     * 日志主键
     */
    @TableId(type = IdType.AUTO)
    @Excel(name = "序号", cellType = ColumnType.NUMERIC)
    private Long logId;

    /**
     * 请求url
     */
    @Excel(name = "请求地址")
    private String requestUrl;

    /**
     * 请求IP
     */
    @Excel(name = "请求IP")
    private String requestIp;

    /**
     * 请求方法
     */
    @Excel(name = "请求方法")
    private String requestMethod;

    /**
     * 命中的URL
     */
    @Excel(name = "命中URL")
    private String hitUrl;

    /**
     * 请求详细
     */
    @Excel(name = "请求详细")
    private String requestDetail;

    /**
     * 响应详细
     */
    @Excel(name = "响应详细")
    private String responseDetail;

}

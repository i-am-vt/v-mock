package com.vmock.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.vmock.base.annotation.Excel;
import lombok.Data;

/**
 * 返回体对象 mock_response
 *
 * @author mock
 * @date 2019-11-20
 */
@Data
@TableName("mock_response")
public class MockResponse extends BaseEntity<MockResponse> {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long responseId;

    /**
     * 所属url
     */
    private Long urlId;

    /**
     * 返回体
     */
    @Excel(name = "返回体")
    private String content;

    /**
     * 返回http状态码
     */
    @Excel(name = "返回http状态码")
    private Integer statusCode;

    /**
     * 是否启用
     */
    @Excel(name = "状态 (1为使用中)")
    private Integer main;

    /**
     * 描述
     */
    @Excel(name = "描述")
    private String description;

    /**
     * 自定义响应头
     */
    @Excel(name = "自定义响应头")
    private String customHeader;

    /**
     * 请求方法
     */
    @Excel(name = "请求方法")
    private String method;

}

package com.vmock.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.vmock.base.annotation.Excel;
import lombok.Data;

/**
 * url路径对象 mock_url
 *
 * @author mock
 * @date 2019-11-20
 */
@Data
@TableName("mock_url")
public class MockUrl extends BaseEntity<MockUrl> {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long urlId;

    /**
     * url路径，path传参位置用{path}占位
     */
    @Excel(name = "url路径，path传参位置用{path}占位")
    private String url;

    /**
     * url名称
     */
    @Excel(name = "url名称")
    private String name;

    /**
     * 描述
     */
    @Excel(name = "描述")
    private String description;

    /**
     * 逻辑符
     */
    @Excel(name = "逻辑符")
    private String logic;

}

package com.vmock.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.vmock.base.annotation.Excel;
import lombok.Data;

/**
 * 子路径对象 mock_url_logic
 *
 * @author mock
 * @date 2019-11-20
 */
@Data
@TableName("mock_url_logic")
public class MockUrlLogic extends BaseEntity<MockUrlLogic> {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long logicId;

    /**
     * 子路径
     */
    @Excel(name = "子路径")
    private String subUrl;

}

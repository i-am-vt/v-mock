package com.vmock.base.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.vmock.base.login.UserContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * common field injector
 * 自动填充创建人 创建日期 修改人 修改日期等公共字段
 *
 * @author vt
 * @since 2019年11月26日
 */
@Component
public class CustomFillHandler implements MetaObjectHandler {

    /**
     * 创建人字段
     */
    private static final String CREATE_BY = "createBy";

    /**
     * 创建时间字段
     */
    private static final String CREATE_TIME = "createTime";

    /**
     * 更新人字段
     */
    private static final String UPDATE_BY = "updateBy";

    /**
     * 更新时间字段
     */
    private static final String UPDATE_TIME = "updateTime";

    /**
     * 插入方法共通填充
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        long currentTime = System.currentTimeMillis();
        // create time
        this.setInsertFieldValByName(CREATE_TIME, currentTime, metaObject);
        // create user id
        this.setInsertFieldValByName(CREATE_BY, UserContext.getUserId(), metaObject);
        // update time
        this.setInsertFieldValByName(UPDATE_TIME, currentTime, metaObject);
        // update user id
        this.setInsertFieldValByName(UPDATE_BY, UserContext.getUserId(), metaObject);
    }

    /**
     * 更新方法共通填充
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // update time
        this.setUpdateFieldValByName(UPDATE_TIME, System.currentTimeMillis(), metaObject);
        // update user id
        this.setUpdateFieldValByName(UPDATE_BY, UserContext.getUserId(), metaObject);
    }
}

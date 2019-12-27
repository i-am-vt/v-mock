package com.vmock.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vmock.biz.entity.MockUrlLogic;

import java.util.List;

/**
 * 子路径Service接口
 *
 * @author mock
 * @date 2019-11-20
 */
public interface IMockUrlLogicService extends IService<MockUrlLogic> {

    /**
     * 根据url批量插入逻辑表
     *
     * @param url 用户输入的url
     * @return 插入后的子urls
     */
    List<MockUrlLogic> insertByUrl(String url);

    /**
     * 通过url查询logic字符串
     *
     * @param url 路径
     * @return logic字符串
     */
    String selectLogicStrByUrl(String url);
}

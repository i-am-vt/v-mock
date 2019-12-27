package com.vmock.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vmock.biz.entity.MockResponse;

import java.util.List;

/**
 * 返回体Service接口
 *
 * @author mock
 * @date 2019-11-20
 */
public interface IMockResponseService extends IService<MockResponse> {

    /**
     * 查询返回体列表
     *
     * @param mockResponse 返回体
     * @return 返回体集合
     */
    List<MockResponse> selectMockResponseList(MockResponse mockResponse);

    /**
     * 修改是否启用
     *
     * @param responseId
     * @param main
     */
    void changeMain(Long responseId, Integer main);

    /**
     * 获取指定url使用中的返回体
     *
     * @param urlId
     * @return MockResponse
     */
    MockResponse selectMainResponse(Long urlId);
}

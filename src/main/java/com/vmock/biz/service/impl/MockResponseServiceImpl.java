package com.vmock.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vmock.base.constant.CommonConst;
import com.vmock.biz.entity.MockResponse;
import com.vmock.biz.enums.MainEnum;
import com.vmock.biz.mapper.MockResponseMapper;
import com.vmock.biz.service.IMockResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 返回体Service业务层处理
 *
 * @author mock
 * @date 2019-11-20
 */
@Service
public class MockResponseServiceImpl extends ServiceImpl<MockResponseMapper, MockResponse> implements IMockResponseService {

    @Autowired
    private MockResponseMapper mockResponseMapper;

    /**
     * 查询返回体列表
     *
     * @param mockResponse 返回体
     * @return 返回体
     */
    @Override
    public List<MockResponse> selectMockResponseList(MockResponse mockResponse) {
        return this.list(Wrappers.<MockResponse>lambdaQuery()
                // 业务过滤 -> url id
                .eq(MockResponse::getUrlId, mockResponse.getUrlId())
                // 条件查询 -> 是否启用
                .eq(mockResponse.getMain() != null, MockResponse::getMain, mockResponse.getMain())
                // 条件查询 -> 简述
                .like(StrUtil.isNotBlank(mockResponse.getDescription()), MockResponse::getDescription, mockResponse.getDescription())
                // 条件查询 -> 简述
                .eq(StrUtil.isNotBlank(mockResponse.getMethod()), MockResponse::getMethod, mockResponse.getMethod()));
    }

    /**
     * 修改是否启用
     *
     * @param responseId
     * @param main
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeMain(Long responseId, Integer main) {
        // 启用
        if (MainEnum.ENABLED.getCode().equals(main)) {
            MockResponse targetEntity = this.getById(responseId);
            // set other disabled
            MockResponse updateModel = new MockResponse();
            updateModel.setMain(MainEnum.DISABLED.getCode());
            mockResponseMapper.update(updateModel, Wrappers.<MockResponse>lambdaUpdate()
                    .eq(MockResponse::getUrlId, targetEntity.getUrlId()));
        }
        // update [main] status
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseId(responseId);
        mockResponse.setMain(main);
        this.updateById(mockResponse);
    }

    /**
     * 获取指定url使用中的返回体
     *
     * @param urlId
     * @return MockResponse
     */
    @Override
    public MockResponse selectMainResponse(Long urlId) {
        return this.getOne(Wrappers.<MockResponse>lambdaQuery()
                .eq(MockResponse::getUrlId, urlId)
                // select enabled record
                .eq(MockResponse::getMain, MainEnum.ENABLED.getCode())
                // limit
                .last(CommonConst.LIMIT_ONE));
    }
}

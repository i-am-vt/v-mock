package com.vmock.biz.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vmock.biz.entity.MockLog;
import com.vmock.biz.mapper.MockLogMapper;
import com.vmock.biz.service.IMockLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志 服务层处理
 *
 * @author mock
 */
@Service
public class MockLogServiceImpl extends ServiceImpl<MockLogMapper, MockLog> implements IMockLogService {

    /**
     * 页面来源 url
     */
    private static final String FROM_URL = "url";

    @Autowired
    private MockLogMapper mockLogMapper;

    /**
     * 查询系统操作日志集合
     *
     * @param mockLog  操作日志对象
     * @param isExport 是否为excel导出
     * @return 操作日志集合
     */
    @Override
    public List<MockLog> selectMockLogList(MockLog mockLog, boolean isExport) {
        return this.list(getFilterWrapper(mockLog, isExport).orderByDesc(MockLog::getCreateTime));
    }

    /**
     * 异步插入
     *
     * @param mockLog log实体
     */
    @Async
    @Override
    public void asyncInsert(MockLog mockLog) {
        mockLog.insert();
    }

    /**
     * 清除指定区间数据
     *
     * @param mockLog log实体
     */
    @Override
    public void clean(MockLog mockLog) {
        this.remove(getFilterWrapper(mockLog, false));
        mockLogMapper.deleteInvalidData();
    }

    /**
     * 获取过滤数据的wrapper
     *
     * @param mockLog  参数实体
     * @param isExport 是否excel导出的过滤，excel导出查询全部字段。
     */
    private LambdaQueryWrapper<MockLog> getFilterWrapper(MockLog mockLog, boolean isExport) {
        Long beginTime = Convert.toLong(mockLog.getParams().get("beginTime"));
        Long endTime = Convert.toLong(mockLog.getParams().get("endTime"));
        String from = Convert.toStr(mockLog.getParams().get("from"));
        LambdaQueryWrapper<MockLog> mockLogWrapper = Wrappers.<MockLog>lambdaQuery()
                // like ip
                .like(StrUtil.isNotBlank(mockLog.getRequestIp()), MockLog::getRequestIp, mockLog.getRequestIp())
                // like hit url
                .like(StrUtil.isNotBlank(mockLog.getHitUrl()) && !FROM_URL.equals(from), MockLog::getHitUrl, mockLog.getHitUrl())
                // equals hit url, in url log page
                .eq(StrUtil.isNotBlank(mockLog.getHitUrl()) && FROM_URL.equals(from), MockLog::getHitUrl, mockLog.getHitUrl())
                // equals method
                .eq(StrUtil.isNotBlank(mockLog.getRequestMethod()), MockLog::getRequestMethod, mockLog.getRequestMethod())
                // beginTime
                .ge(beginTime != null, MockLog::getCreateTime, beginTime)
                // endTime 加入当日的时间戳
                .le(endTime != null, MockLog::getCreateTime, endTime);
        // 非导出场景
        if (!isExport) {
            // 只查询需要字段，排除大json字段
            mockLogWrapper.select(MockLog::getLogId, MockLog::getHitUrl, MockLog::getRequestMethod, MockLog::getCreateTime, MockLog::getRequestIp);
        }
        return mockLogWrapper;
    }
}

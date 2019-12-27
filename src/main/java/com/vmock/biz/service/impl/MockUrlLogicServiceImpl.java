package com.vmock.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vmock.base.constant.CommonConst;
import com.vmock.biz.entity.MockUrlLogic;
import com.vmock.biz.mapper.MockUrlLogicMapper;
import com.vmock.biz.service.IMockUrlLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 子路径Service业务层处理
 *
 * @author mock
 * @date 2019-11-20
 */
@Service
public class MockUrlLogicServiceImpl extends ServiceImpl<MockUrlLogicMapper, MockUrlLogic> implements IMockUrlLogicService {

    @Autowired
    private MockUrlLogicMapper mockUrlLogicMapper;

    /**
     * 根据url批量插入逻辑表
     *
     * @param url 用户输入的url
     * @return 插入后的子urls
     */
    @Override
    public List<MockUrlLogic> insertByUrl(String url) {
        // 根据斜线分割 插入逻辑表
        String[] subUrls = url.split("\\/");
        List<MockUrlLogic> mockUrlLogics = new ArrayList<>();
        // 做成集合批量插入
        //  db sub_url是 UNIQUE，并 ON CONFLICT IGNORE，不会重复。
        MockUrlLogic mockUrlLogic;
        for (String subUrl : subUrls) {
            // 空 不记录
            if (StrUtil.isBlank(subUrl)) {
                continue;
            }
            // 存入集合
            mockUrlLogic = new MockUrlLogic();
            mockUrlLogic.setSubUrl(subUrl);
            mockUrlLogics.add(mockUrlLogic);
        }
        this.saveBatch(mockUrlLogics);
        // 查询保存过，或已存在的record
        List<MockUrlLogic> queryResult = this.list(Wrappers.<MockUrlLogic>lambdaQuery().in(MockUrlLogic::getSubUrl, subUrls));
        return queryResult;
    }

    /**
     * 通过url查询logic字符串
     *
     * @param url 路径
     * @return logic字符串
     */
    @Override
    public String selectLogicStrByUrl(String url) {
        // 拆分为子url
        List<String> subUrls = StrUtil.splitTrim(url, StrUtil.C_SLASH);
        // empty -> put '/'
        if (subUrls.isEmpty()) {
            subUrls.add(StrUtil.SLASH);
        }
        // 查询对应logicId
        List<MockUrlLogic> urlLogics = this.list(Wrappers.<MockUrlLogic>lambdaQuery()
                // in 查询请求的url
                .in(MockUrlLogic::getSubUrl, subUrls));
        // 转为map
        Map<String, Long> subUrlAndLogicId = urlLogics.stream().collect(Collectors.toMap(MockUrlLogic::getSubUrl, MockUrlLogic::getLogicId));
        StringJoiner logicStrJoiner = new StringJoiner(StrUtil.COMMA);
        // 删除空元素，循环拼接
        subUrls.stream().filter(StrUtil::isNotBlank).forEach(item -> {
            // 2.get logicId, ※ 如果是null，那么推测此处用户用了path传参, 则返回占位符！
            Long logicId = subUrlAndLogicId.get(item);
            // 拼接
            logicStrJoiner.add(logicId == null ? CommonConst.PATH_PLACEHOLDER : logicId.toString());
        });
        return logicStrJoiner.toString();
    }
}

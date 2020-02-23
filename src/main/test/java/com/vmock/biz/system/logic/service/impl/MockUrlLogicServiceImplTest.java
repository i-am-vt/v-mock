package com.vmock.biz.system.logic.service.impl;

import cn.hutool.json.JSONUtil;
import com.vmock.biz.entity.MockUrlLogic;
import com.vmock.biz.service.IMockUrlLogicService;
import common.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MockUrlLogicServiceImplTest extends TestBase {

    @Autowired
    private IMockUrlLogicService mockUrlLogicService;

    @Test
    public void insertByUrl() {
        List<MockUrlLogic> mockUrlLogics = mockUrlLogicService.insertByUrl("/v1/v2");
        System.out.println(JSONUtil.toJsonPrettyStr(mockUrlLogics));
    }

    @Test
    public void selectLogicStrByUrl() {
        String logicStr = mockUrlLogicService.selectLogicStrByUrl("/v1/sdfdsf/v2");
        System.out.println(logicStr);
    }
}
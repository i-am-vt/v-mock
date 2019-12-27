package com.vmock.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vmock.biz.entity.MockUrl;

import java.util.List;

/**
 * url路径Service接口
 *
 * @author mock
 * @date 2019-11-20
 */
public interface IMockUrlService extends IService<MockUrl> {

    /**
     * 根据Url查询
     *
     * @param url url路径
     * @return MockUrl
     */
    MockUrl selectMockUrlByUrl(String url);

    /**
     * 根据logic查询
     *
     * @param logic logic字符串
     * @return MockUrl
     */
    MockUrl selectMockUrlByLogic(String logic);

    /**
     * 查询url路径列表
     *
     * @param mockUrl url路径
     * @return url路径集合
     */
    List<MockUrl> selectMockUrlList(MockUrl mockUrl);

    /**
     * 新增url路径
     *
     * @param mockUrl url路径
     * @return 结果
     */
    boolean insertMockUrl(MockUrl mockUrl);

    /**
     * 修改url路径
     *
     * @param mockUrl url路径
     * @return 结果
     */
    boolean updateMockUrl(MockUrl mockUrl);

    /**
     * 批量删除url路径
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    boolean deleteMockUrlByIds(String ids);

    /**
     * 处理url为logic
     *
     * @param url 入参url
     * @return 处理好的logic字段
     */
    String insertUrlLogic(String url);

    /**
     * 处理url格式
     *
     * @param url url格式
     * @return 简单处理后的url
     */
    String formatUrlStr(String url);

    /**
     * url unique check
     *
     * @param url   url path
     * @param urlId has id, for edit page
     * @return true：唯一url， false 不唯一
     */
    boolean isUniqueUrl(String url, Long urlId);

    /**
     * 根据逻辑创建正则查url
     *
     * @param requestUrlLogic logic string
     * @return mockUrl entity
     */
    MockUrl getUrlByRegexp(String requestUrlLogic);
}

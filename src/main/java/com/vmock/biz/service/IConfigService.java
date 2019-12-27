package com.vmock.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vmock.biz.entity.Config;

import java.util.List;

/**
 * 参数配置 服务层
 *
 * @author mock
 */
public interface IConfigService extends IService<Config> {

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    String getVal(String configKey);

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    List<Config> selectConfigList(Config config);

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数信息
     * @return 结果
     */
    Boolean checkConfigKeyUnique(Config config);

}

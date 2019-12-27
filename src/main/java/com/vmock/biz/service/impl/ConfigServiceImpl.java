package com.vmock.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vmock.base.constant.CommonConst;
import com.vmock.biz.entity.Config;
import com.vmock.biz.mapper.ConfigMapper;
import com.vmock.biz.service.IConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.hutool.core.util.StrUtil.EMPTY;

/**
 * 参数配置 服务层实现
 *
 * @author mock
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数名称
     * @return 参数键值
     */
    @Override
    public String getVal(String configKey) {
        Config config = new Config();
        config.setConfigKey(configKey);
        Config retConfig = this.getOne(Wrappers.<Config>lambdaQuery().eq(Config::getConfigKey, configKey));
        return retConfig != null ? retConfig.getConfigValue() : EMPTY;
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<Config> selectConfigList(Config config) {
        return this.list(Wrappers.<Config>lambdaQuery()
                // query by name ,if not blank
                .like(StrUtil.isNotBlank(config.getConfigName()), Config::getConfigName, config.getConfigName()));
    }


    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public Boolean checkConfigKeyUnique(Config config) {
        Long configId = config.getConfigId() == null ? CommonConst.INVAILD_ID : config.getConfigId();
        Config info = this.getOne(Wrappers.<Config>lambdaQuery().eq(Config::getConfigKey, config.getConfigKey()));
        if (info != null && info.getConfigId().longValue() != configId.longValue()) {
            return false;
        }
        return true;
    }
}

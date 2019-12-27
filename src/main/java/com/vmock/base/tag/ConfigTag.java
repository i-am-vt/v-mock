package com.vmock.base.tag;

import com.vmock.biz.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 配置tag，tag用于前端调用，写法类似旧款的那种jsp自定义标签
 *
 * @author mock
 */
@Service("config")
public class ConfigTag {

    @Autowired
    private IConfigService configService;

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数名称
     * @return 参数键值
     */
    public String getKey(String configKey) {
        return configService.getVal(configKey);
    }

}

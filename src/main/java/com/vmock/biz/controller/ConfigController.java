package com.vmock.biz.controller;

import cn.hutool.core.util.StrUtil;
import com.vmock.base.vo.Result;
import com.vmock.base.vo.TableDataVo;
import com.vmock.biz.entity.Config;
import com.vmock.biz.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author mock
 */
@Controller
@RequestMapping("/system/config")
public class ConfigController extends BaseController {

    private static final String PREFIX = "system/config";

    @Autowired
    private IConfigService configService;


    @GetMapping
    public String config() {
        return PREFIX + "/config";
    }

    /**
     * 查询参数配置列表
     *
     * @param config
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataVo<Config> list(Config config) {
        startPage();
        List<Config> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    /**
     * 修改参数配置
     *
     * @param configId
     * @param mmap
     */
    @GetMapping("/edit/{configId}")
    public String edit(@PathVariable("configId") Long configId, ModelMap mmap) {
        mmap.put("config", configService.getById(configId));
        return PREFIX + "/edit";
    }

    /**
     * 修改保存参数配置
     *
     * @param config
     */
    @PostMapping("/edit")
    @ResponseBody
    public Result<Void> editSave(@Validated Config config) {
        return create(configService.updateById(config));
    }

    /**
     * 删除参数配置
     *
     * @param ids
     */
    @PostMapping("/remove")
    @ResponseBody
    public Result<Void> remove(String ids) {
        return create(configService.removeByIds(StrUtil.splitTrim(ids, StrUtil.C_COMMA)));
    }

    /**
     * 校验参数键名
     *
     * @param config
     */
    @PostMapping("/checkConfigKeyUnique")
    @ResponseBody
    public Boolean checkConfigKeyUnique(Config config) {
        return configService.checkConfigKeyUnique(config);
    }
}

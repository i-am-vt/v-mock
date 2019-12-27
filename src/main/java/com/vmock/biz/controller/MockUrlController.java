package com.vmock.biz.controller;

import com.vmock.base.utils.ExcelUtil;
import com.vmock.base.vo.Result;
import com.vmock.base.vo.TableDataVo;
import com.vmock.biz.entity.MockUrl;
import com.vmock.biz.service.IMockUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * url路径Controller
 *
 * @author mock
 * @date 2019-11-20
 */
@Controller
@RequestMapping("/system/url")
public class MockUrlController extends BaseController {

    private static final String PREFIX = "system/url";

    @Autowired
    private IMockUrlService mockUrlService;


    @GetMapping
    public String url() {
        return PREFIX + "/url";
    }

    /**
     * 查询url路径列表
     */

    @PostMapping("/list")
    @ResponseBody
    public TableDataVo<MockUrl> list(MockUrl mockUrl) {
        startPage();
        List<MockUrl> list = mockUrlService.selectMockUrlList(mockUrl);
        return getDataTable(list);
    }

    /**
     * 导出url路径列表
     */


    @PostMapping("/export")
    @ResponseBody
    public Result<Void> export(MockUrl mockUrl) {
        List<MockUrl> list = mockUrlService.selectMockUrlList(mockUrl);
        ExcelUtil<MockUrl> util = new ExcelUtil<MockUrl>(MockUrl.class);
        return util.exportExcel(list, "url");
    }

    /**
     * 新增url路径
     */
    @GetMapping("/add")
    public String add() {
        return PREFIX + "/add";
    }

    /**
     * 新增保存url路径
     */


    @PostMapping("/add")
    @ResponseBody
    public Result addSave(MockUrl mockUrl) {
        return create(mockUrlService.insertMockUrl(mockUrl));
    }

    /**
     * 修改url路径
     */
    @GetMapping("/edit/{urlId}")
    public String edit(@PathVariable("urlId") Long urlId, ModelMap mmap) {
        MockUrl mockUrl = mockUrlService.getById(urlId);
        mmap.put("mockUrl", mockUrl);
        return PREFIX + "/edit";
    }

    /**
     * 修改保存url路径
     */


    @PostMapping("/edit")
    @ResponseBody
    public Result editSave(MockUrl mockUrl) {
        return create(mockUrlService.updateMockUrl(mockUrl));
    }

    /**
     * 删除url
     */
    @DeleteMapping
    @ResponseBody
    public Result remove(String ids) {
        return create(mockUrlService.deleteMockUrlByIds(ids));
    }


    /**
     * url唯一校验
     */
    @GetMapping("/check")
    @ResponseBody
    public Boolean checkUrl(String url, Long urlId) {
        return mockUrlService.isUniqueUrl(url, urlId);
    }


    /**
     * 详情页
     */
    @GetMapping("/{urlId}")
    public String detail(@PathVariable("urlId") Long urlId, ModelMap mmap) {
        MockUrl mockUrl = mockUrlService.getById(urlId);
        mmap.put("mockUrl", mockUrl);
        return PREFIX + "/detail";
    }

}

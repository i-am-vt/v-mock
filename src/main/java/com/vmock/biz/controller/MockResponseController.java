package com.vmock.biz.controller;

import com.vmock.base.utils.ExcelUtil;
import com.vmock.base.vo.Result;
import com.vmock.base.vo.TableDataVo;
import com.vmock.biz.entity.MockResponse;
import com.vmock.biz.service.IMockResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 返回体Controller
 *
 * @author mock
 * @date 2019-11-20
 */
@Controller
@RequestMapping("/system/response")
public class MockResponseController extends BaseController {

    private static final String PREFIX = "system/response";

    @Autowired
    private IMockResponseService mockResponseService;


    @GetMapping
    public String response(String urlId, ModelMap mmap) {
        mmap.put("urlId", urlId);
        return PREFIX + "/response";
    }

    /**
     * 查询返回体列表
     */

    @PostMapping("/list")
    @ResponseBody
    public TableDataVo<MockResponse> list(MockResponse mockResponse) {
        startPage();
        List<MockResponse> list = mockResponseService.selectMockResponseList(mockResponse);
        return getDataTable(list);
    }

    /**
     * 导出返回体列表
     */
    @PostMapping("/export")
    @ResponseBody
    public Result<Void> export(MockResponse mockResponse) {
        List<MockResponse> list = mockResponseService.selectMockResponseList(mockResponse);
        ExcelUtil<MockResponse> util = new ExcelUtil<>(MockResponse.class);
        return util.exportExcel(list, "response");
    }

    /**
     * 新增返回体
     */
    @GetMapping("/add")
    public String add(String urlId, ModelMap mmap) {
        mmap.put("urlId", urlId);
        return PREFIX + "/add";
    }

    /**
     * 新增保存返回体
     */
    @PostMapping("/add")
    @ResponseBody
    public Result addSave(MockResponse mockResponse) {
        return create(mockResponseService.save(mockResponse));
    }

    /**
     * 修改返回体
     */
    @GetMapping("/edit/{responseId}")
    public String edit(@PathVariable("responseId") Long responseId, ModelMap mmap) {
        MockResponse mockResponse = mockResponseService.getById(responseId);
        mmap.put("mockResponse", mockResponse);
        return PREFIX + "/edit";
    }

    /**
     * 修改保存返回体
     */
    @PostMapping("/edit")
    @ResponseBody
    public Result editSave(MockResponse mockResponse) {
        return create(mockResponseService.updateById(mockResponse));
    }


    /**
     * 详情页
     */
    @GetMapping("/{responseId}")
    public String detail(@PathVariable("responseId") Long urlId, ModelMap mmap) {
        MockResponse mockResponse = mockResponseService.getById(urlId);
        mmap.put("mockResponse", mockResponse);
        return PREFIX + "/detail";
    }

    /**
     * 启用停用
     */
    @PutMapping("/{responseId}/main")
    @ResponseBody
    public Result<Void> changeMain(@PathVariable(name = "responseId") Long responseId, @RequestParam Integer main) {
        mockResponseService.changeMain(responseId, main);
        return Result.success();
    }

    /**
     * 删除返回体
     */
    @DeleteMapping
    @ResponseBody
    public Result remove(String ids) {
        return create(mockResponseService.removeById(ids));
    }
}

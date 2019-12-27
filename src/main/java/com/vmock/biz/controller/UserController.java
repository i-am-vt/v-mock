package com.vmock.biz.controller;

import cn.hutool.core.util.StrUtil;
import com.vmock.base.login.UserContext;
import com.vmock.base.utils.ExcelUtil;
import com.vmock.base.vo.Result;
import com.vmock.base.vo.TableDataVo;
import com.vmock.biz.entity.User;
import com.vmock.biz.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户信息
 *
 * @author mock
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    private static final String PREFIX = "system/user";

    @Autowired
    private IUserService userService;

    @GetMapping
    public String user() {
        return PREFIX + "/user";
    }

    /**
     * 用户列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataVo<User> list(User user) {
        startPage();
        List<User> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 导出
     */
    @PostMapping("/export")
    @ResponseBody
    public Result<Void> export(User user) {
        List<User> list = userService.selectUserList(user);
        ExcelUtil<User> util = new ExcelUtil<>(User.class);
        return util.exportExcel(list, "用户数据");
    }


    /**
     * 新增用户
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        return PREFIX + "/add";
    }

    /**
     * 新增保存用户
     */
    @PostMapping("/add")
    @ResponseBody
    public Result addSave(@Validated User user) {
        if (!userService.checkLoginNameUnique(user.getLoginName())) {
            return error("新增用户'" + user.getLoginName() + "'失败，登录账号已存在");
        }
        return create(userService.save(user));
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, ModelMap mmap) {
        mmap.put("user", userService.getById(userId));
        return PREFIX + "/edit";
    }

    /**
     * 修改保存用户
     */
    @PutMapping
    @ResponseBody
    public Result editSave(@Validated User user) {
        return create(userService.updateById(user));
    }


    @GetMapping("/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap) {
        mmap.put("user", userService.getById(userId));
        return PREFIX + "/resetPwd";
    }


    @PutMapping("/password")
    @ResponseBody
    public Result resetPwdSave(User user) {
        if (userService.resetUserPwd(user)) {
            if (UserContext.getUserId().equals(user.getUserId())) {
                setSysUser(userService.getById(user.getUserId()));
            }
            return success();
        }
        return error();
    }


    @DeleteMapping
    @ResponseBody
    public Result remove(String ids) {
        return create(userService.removeByIds(StrUtil.splitTrim(ids, StrUtil.COMMA)));
    }

    /**
     * 校验用户名
     */
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public Boolean checkLoginNameUnique(User user) {
        return userService.checkLoginNameUnique(user.getLoginName());
    }

    /**
     * 用户状态修改
     */
    @PostMapping("/changeStatus")
    @ResponseBody
    public Result changeStatus(User user) {
        userService.checkUserAllowed(user);
        return create(userService.updateById(user));
    }
}
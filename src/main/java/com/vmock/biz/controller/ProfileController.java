package com.vmock.biz.controller;

import cn.hutool.core.util.StrUtil;
import com.vmock.base.login.PasswordService;
import com.vmock.base.vo.Result;
import com.vmock.biz.entity.User;
import com.vmock.biz.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 个人信息 业务处理
 *
 * @author mock
 */
@Controller
@RequestMapping("/system/user/profile")
public class ProfileController extends BaseController {


    private static final String PREFIX = "system/user/profile";

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordService passwordService;

    /**
     * 个人信息
     */
    @GetMapping
    public String profile(ModelMap mmap) {
        User user = getSysUser();
        mmap.put("user", user);
        return PREFIX + "/profile";
    }

    @GetMapping("/checkPassword")
    @ResponseBody
    public boolean checkPassword(String password) {
        User user = getSysUser();
        if (passwordService.matches(user, password)) {
            return true;
        }
        return false;
    }

    @GetMapping("/resetPwd")
    public String resetPwd(ModelMap mmap) {
        User user = getSysUser();
        mmap.put("user", userService.getById(user.getUserId()));
        return PREFIX + "/resetPwd";
    }


    @PutMapping("/password")
    @ResponseBody
    public Result<Void> resetPwd(String oldPassword, String newPassword) {
        User user = getSysUser();
        if (StrUtil.isNotEmpty(newPassword) && passwordService.matches(user, oldPassword)) {
            user.setPassword(newPassword);
            if (userService.resetUserPwd(user)) {
                setSysUser(userService.getById(user.getUserId()));
                return success();
            }
            return error();
        } else {
            return error("修改密码失败，旧密码错误");
        }

    }

    /**
     * 修改用户
     */
    @GetMapping("/edit")
    public String edit(ModelMap mmap) {
        User user = getSysUser();
        mmap.put("user", userService.getById(user.getUserId()));
        return PREFIX + "/edit";
    }


    /**
     * 修改用户
     */
    @PostMapping("/update")
    @ResponseBody
    public Result update(User user) {
        User currentUser = getSysUser();
        currentUser.setUserName(user.getUserName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setSex(user.getSex());
        if (userService.updateById(currentUser)) {
            setSysUser(userService.getById(currentUser.getUserId()));
            return success();
        }
        return error();
    }
}

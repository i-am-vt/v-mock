package com.vmock.biz.controller;

import com.vmock.biz.entity.Menu;
import com.vmock.biz.entity.User;
import com.vmock.biz.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 首页 业务处理
 *
 * @author mock
 */
@Controller
public class IndexController extends BaseController {

    @Autowired
    private IMenuService menuService;

    /**
     * 系统首页
     */
    @GetMapping("/index")
    public String index(ModelMap mmap) {
        // 取身份信息
        User user = getSysUser();
        // 根据用户id取出菜单
        List<Menu> menus = menuService.selectMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        return "index";
    }

    /**
     * 主页
     */
    @GetMapping("/system/main")
    public String main() {
        return "main";
    }
}

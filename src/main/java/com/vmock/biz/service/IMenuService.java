package com.vmock.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vmock.biz.entity.Menu;
import com.vmock.biz.entity.User;

import java.util.List;

/**
 * 菜单 业务层
 *
 * @author mock
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据用户ID查询菜单
     *
     * @param user 用户信息
     * @return 菜单列表
     */
    List<Menu> selectMenusByUser(User user);
}

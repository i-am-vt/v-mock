package com.vmock.biz.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vmock.biz.entity.Menu;
import com.vmock.biz.entity.User;
import com.vmock.biz.enums.UserDisplayEnum;
import com.vmock.biz.enums.UserTypeEnum;
import com.vmock.biz.mapper.MenuMapper;
import com.vmock.biz.service.IMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单 业务层处理
 *
 * @author mock
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    /**
     * 根据用户查询菜单
     *
     * @param user 用户信息
     * @return 菜单列表
     */
    @Override
    public List<Menu> selectMenusByUser(User user) {
        // only admin show all
        List<Menu> menus = this.list(Wrappers.<Menu>lambdaQuery()
                // if not admin, query nomorl user menu
                .eq(!UserTypeEnum.ADMIN.getCode().equals(user.getUserType()), Menu::getUserDisplay, UserDisplayEnum.USER_DISPLAY.getCode())
                // order by order number
                .orderByAsc(Menu::getOrderNum));
        return menus;
    }
}

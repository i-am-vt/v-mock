package com.vmock.base.login;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.vmock.base.exception.UserBlockedException;
import com.vmock.base.exception.UserNotExistsException;
import com.vmock.biz.entity.User;
import com.vmock.biz.enums.UserStatus;
import com.vmock.biz.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录校验方法
 *
 * @author mock
 */
@Component
public class LoginService {

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private IUserService userService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 登录
     */
    public User login(String username, String password) {
        // 用户名或密码为空 错误
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            throw new UserNotExistsException();
        }
        // 查询用户信息
        User user = userService.selectUserByLoginName(username);
        if (user == null) {
            throw new UserNotExistsException();
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            throw new UserBlockedException();
        }
        passwordService.validate(user, password);
        recordLoginInfo(user);
        return user;
    }

    /**
     * 记录登录信息
     */
    private void recordLoginInfo(User user) {
        user.setLoginIp(ServletUtil.getClientIP(request));
        user.setLoginDate(System.currentTimeMillis());
        user.updateById();
    }
}

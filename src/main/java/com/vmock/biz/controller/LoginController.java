package com.vmock.biz.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import com.vmock.base.constant.LoginConst;
import com.vmock.base.utils.ContextUtils;
import com.vmock.base.vo.Result;
import com.vmock.biz.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录验证
 *
 * @author mock
 */
@Controller
public class LoginController extends BaseController {


    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        // 如果是Ajax请求，返回Json字符串。
        if (ContextUtils.isAjAx(request)) {
            ServletUtil.write(response, LoginConst.TIME_OUT, ContentType.JSON.toString(CharsetUtil.CHARSET_UTF_8));
            return null;
        }
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Result<Void> ajaxLogin(String username, String password) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, true);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return success();
        } catch (AuthenticationException e) {
            String msg = "用户或密码错误";
            if (StrUtil.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }

    @GetMapping("/unauth")
    public String unauth() {
        return "error/unauth";
    }


    @PostMapping("/register")
    @ResponseBody
    public Result register(String username, String invitationCode, String password) {
        userService.register(username, invitationCode, password);
        return success();
    }
}

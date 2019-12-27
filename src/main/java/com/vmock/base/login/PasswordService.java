package com.vmock.base.login;

import com.vmock.base.exception.UserPasswordNotMatchException;
import com.vmock.biz.entity.User;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Component;

/**
 * 登录密码方法
 *
 * @author mock
 */
@Component
public class PasswordService {

    public void validate(User user, String password) {
        if (!matches(user, password)) {
            throw new UserPasswordNotMatchException();
        }
    }

    public boolean matches(User user, String newPassword) {
        return user.getPassword().equals(encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
    }

    public String encryptPassword(String username, String password, String salt) {
        return new Md5Hash(username + password + salt).toHex();
    }
}

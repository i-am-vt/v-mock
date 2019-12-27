package com.vmock.base.login;

import cn.hutool.core.bean.BeanUtil;
import com.vmock.biz.entity.User;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 * shiro 工具类
 *
 * @author mock
 */
@Slf4j
@UtilityClass
public class UserContext {

    public static User getSysUser() {
        User user = null;
        Object obj = getSubject().getPrincipal();
        if (obj != null) {
            user = new User();
            BeanUtil.copyProperties(obj, user);
        }
        return user;
    }

    public static void setSysUser(User user) {
        Subject subject = getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user, realmName);
        // 重新加载Principal
        subject.runAs(newPrincipalCollection);
    }

    public static void clearCachedAuthorizationInfo() {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        CustomRealm realm = (CustomRealm) rsm.getRealms().iterator().next();
        realm.clearCachedAuthorizationInfo();
    }

    public static Long getUserId() {
        try {
            User sysUser = getSysUser();
            return sysUser == null ? null : sysUser.getUserId();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getLoginName() {
        return getSysUser().getLoginName();
    }

    public static String getIp() {
        return getSubject().getSession().getHost();
    }

    public static String getSessionId() {
        return String.valueOf(getSubject().getSession().getId());
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static void logout() {
        getSubject().logout();
    }
}

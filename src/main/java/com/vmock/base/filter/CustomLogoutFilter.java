package com.vmock.base.filter;

import cn.hutool.core.util.StrUtil;
import com.vmock.base.constant.LoginConst;
import com.vmock.base.login.UserContext;
import com.vmock.biz.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;

/**
 * 退出过滤器
 *
 * @author mock
 */
@Slf4j
public class CustomLogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {

    /**
     * 退出后重定向的地址
     */
    @Getter
    @Setter
    private String loginUrl;

    private Cache<String, Deque<Serializable>> cache;

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        try {
            Subject subject = getSubject(request, response);
            String redirectUrl = getRedirectUrl(request, response, subject);
            User user = UserContext.getSysUser();
            if (user != null) {
                String loginName = user.getLoginName();
                // 清理缓存
                cache.remove(loginName);
            }
            // 退出登录
            subject.logout();
            issueRedirect(request, response, redirectUrl);
        } catch (Exception e) {
            log.error("Encountered session exception during logout.  This can generally safely be ignored.", e);
        }
        return false;
    }

    /**
     * 退出跳转URL
     */
    @Override
    protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject) {
        String url = getLoginUrl();
        if (StrUtil.isNotEmpty(url)) {
            return url;
        }
        return super.getRedirectUrl(request, response, subject);
    }

    /**
     * 设置Cache的key的前缀
     *
     * @param cacheManager 缓存管理器
     */
    public void setCacheManager(CacheManager cacheManager) {
        // 必须和ehcache缓存配置中的缓存name一致
        this.cache = cacheManager.getCache(LoginConst.SYS_USER_CACHE);
    }
}

package com.vmock.base.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 主要业务过滤器
 *
 * @author vt
 * @since 2019年12月4日
 */
@Slf4j
public class MockFilter implements Filter {

    /**
     * 逻辑处理类
     */
    private MockFilterHandler mockFilterHandler;

    /**
     * bean 辅助
     */
    private ApplicationContext applicationContext;

    /**
     * 初始化
     *
     * @param filterConfig 配置
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        // init applicationContext
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        // init mockFilterHandler
        mockFilterHandler = applicationContext.getBean(MockFilterHandler.class);
    }

    /**
     * 业务实现
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 转换类型
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        // 执行主要逻辑处理
        mockFilterHandler.execute(httpServletRequest, httpServletResponse);
    }
}

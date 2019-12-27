package com.vmock.base.filter;

import cn.hutool.http.HtmlUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * XSS过滤处理
 *
 * @author mock
 */
public class TagHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /**
     * @param request
     */
    public TagHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                // 防xss攻击和过滤前后空格
                escapseValues[i] = HtmlUtil.escape(values[i]).trim();
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }
}
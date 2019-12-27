package com.vmock.base.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.vmock.base.filter.TagFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.Map;

/**
 * Filter配置
 *
 * @author mock
 */
@Configuration
public class FilterConfig {

    private static final String EXCLUDES = "/system/response/*";

    private static final String URL_PATTERNS = "/system/*";

    @Bean
    public FilterRegistrationBean tagFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new TagFilter());
        registration.addUrlPatterns(StrUtil.split(URL_PATTERNS, ","));
        registration.setName("tagFilter");
        registration.setOrder(Integer.MAX_VALUE);
        Map<String, String> initParameters = CollUtil.newHashMap();
        initParameters.put("excludes", EXCLUDES);
        registration.setInitParameters(initParameters);
        return registration;
    }
}

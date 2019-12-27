package com.vmock.base.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.github.pagehelper.PageInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * orm层配置
 *
 * @author vt
 * @since 2019年12月16日
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.vmock.biz.mapper")
public class OrmConfig {

    /**
     * 适配pagehelper代码
     */
    @Bean
    public PageInterceptor paginationInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("helperDialect", DbType.SQLITE.getDb());
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("params", "count=countSql");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }
}
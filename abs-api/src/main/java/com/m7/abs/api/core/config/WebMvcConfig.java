package com.m7.abs.api.core.config;

import com.m7.abs.api.core.interceptor.AuthorityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author pkj
 * @create 2018-05-14 下午 4:39
 **/
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Autowired
    private AuthorityInterceptor authorityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorityInterceptor).addPathPatterns("/**");
    }
}

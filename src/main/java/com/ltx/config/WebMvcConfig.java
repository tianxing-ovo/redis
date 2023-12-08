package com.ltx.config;

import com.ltx.interceptor.LoginInterceptor;
import com.ltx.interceptor.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * web配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private RefreshTokenInterceptor refreshTokenInterceptor;

    @Resource
    private LoginInterceptor loginInterceptor;

    /**
     * 添加拦截器
     * addPathPatterns: 拦截路径
     * excludePathPatterns: 不拦截路径
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(refreshTokenInterceptor).order(1);
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**").
                excludePathPatterns("/login").order(2);
    }
}

package com.ruleframe.web.config;

import com.ruleframe.web.interceptor.TokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 * 注册Token认证拦截器
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/**")  // 拦截所有API请求
                .excludePathPatterns(
                        "/api/auth/login",   // 排除登录接口
                        "/api/auth/register", // 排除注册接口（如果有）
                        "/api/public/**",    // 排除公开接口
                        "/api/doc/**",       // 排除API文档
                        "/swagger-ui/**",    // 排除Swagger UI
                        "/v3/api-docs/**"    // 排除API文档
                );
    }
}

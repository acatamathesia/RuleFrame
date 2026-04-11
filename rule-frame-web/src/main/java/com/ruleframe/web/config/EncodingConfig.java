package com.ruleframe.web.config;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;

/**
 * 全局编码配置
 * 确保整个应用使用 UTF-8 编码
 */
@Configuration
public class EncodingConfig {

    /**
     * 配置字符串消息转换器使用 UTF-8 编码
     */
    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }
}

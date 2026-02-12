package com.ruleframe.web.config;

import com.ruleframe.core.RuleEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RuleEngineConfig {

    @Bean
    public RuleEngine ruleEngine() {
        return new RuleEngine();
    }
}
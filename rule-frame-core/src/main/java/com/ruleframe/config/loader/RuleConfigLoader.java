package com.ruleframe.config.loader;

import com.ruleframe.core.group.RuleGroup;

/**
 * 规则配置加载器接口
 */
public interface RuleConfigLoader {

    /**
     * 根据键加载规则组
     * @param key 配置键
     * @return 规则组
     */
    RuleGroup load(String key);
}
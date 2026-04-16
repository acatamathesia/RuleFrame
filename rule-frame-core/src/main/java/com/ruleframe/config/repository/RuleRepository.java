package com.ruleframe.config.repository;

import com.ruleframe.core.group.RuleGroup;

/**
 * 统一对外提供获取规则组的能力
 */
public interface RuleRepository {

    /**
     * 根据键获取规则组
     * @param key 配置键
     * @return 规则组
     */
    RuleGroup getRuleGroup(String key);
}
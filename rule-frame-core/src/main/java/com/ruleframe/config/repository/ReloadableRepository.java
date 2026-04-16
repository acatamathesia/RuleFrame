package com.ruleframe.config.repository;

import com.ruleframe.core.group.RuleGroup;

/**
 * 支持监听配置变更、热更新
 */
public class ReloadableRepository implements RuleRepository {

    @Override
    public RuleGroup getRuleGroup(String key) {
        // TODO: 实现可热更新的规则仓库逻辑
        return null;
    }

    /**
     * 重新加载配置
     */
    public void reload() {
        // TODO: 实现配置重载逻辑
    }

    /**
     * 添加配置变更监听器
     * @param listener 监听器
     */
    public void addChangeListener(Runnable listener) {
        // TODO: 实现监听器注册逻辑
    }
}
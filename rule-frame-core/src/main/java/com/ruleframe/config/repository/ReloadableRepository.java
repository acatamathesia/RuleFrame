package com.ruleframe.config.repository;

import com.ruleframe.config.loader.RuleConfigLoader;
import com.ruleframe.core.group.RuleGroup;

/**
 * 支持监听配置变更、热更新的规则仓库
 * <p>
 * 通过组合 {@link RuleConfigLoader} 实现数据获取，自身专注于热更新与变更通知能力。
 * </p>
 */
public class ReloadableRepository implements RuleRepository {

    private final RuleConfigLoader loader;

    /**
     * @param loader 底层规则配置加载器，用于从数据源加载规则组
     */
    public ReloadableRepository(RuleConfigLoader loader) {
        this.loader = loader;
    }

    @Override
    public RuleGroup getRuleGroup(String key) {
        return loader.load(key);
    }

    /**
     * 重新加载配置
     */
    public void reload() {
        // TODO: 实现配置重载逻辑（如清除缓存后重新加载）
    }

    /**
     * 添加配置变更监听器
     * 
     * @param listener 监听器
     */
    public void addChangeListener(Runnable listener) {
        // TODO: 实现监听器注册逻辑
    }
}
package com.ruleframe.config.loader;

import com.ruleframe.core.group.RuleGroup;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 装饰器，增加缓存功能
 */
public class CachedLoader implements RuleConfigLoader {

    private final RuleConfigLoader delegate;
    private final Map<String, RuleGroup> cache = new ConcurrentHashMap<>();

    public CachedLoader(RuleConfigLoader delegate) {
        this.delegate = delegate;
    }

    @Override
    public RuleGroup load(String key) {
        // TODO: 实现缓存逻辑
        return cache.computeIfAbsent(key, delegate::load);
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        cache.clear();
    }

    /**
     * 清除指定键的缓存
     * @param key 配置键
     */
    public void clearCache(String key) {
        cache.remove(key);
    }
}
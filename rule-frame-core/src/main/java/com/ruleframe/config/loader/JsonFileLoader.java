package com.ruleframe.config.loader;

import com.ruleframe.core.group.RuleGroup;

/**
 * JSON文件加载器
 */
public class JsonFileLoader implements RuleConfigLoader {

    /**
     * 规则样式
     * {
     * 'groupId': 'GP001',
     * 'groupName': '测试规则组'
     * }
     */
    @Override
    public RuleGroup load(String key) {
        // TODO: 从JSON文件加载规则组配置
        return null;
    }
}
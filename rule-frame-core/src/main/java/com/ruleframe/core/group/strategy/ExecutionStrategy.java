package com.ruleframe.core.group.strategy;

import com.ruleframe.core.group.GroupResult;
import com.ruleframe.core.group.RuleGroup;
import com.ruleframe.core.fact.FactContext;

/**
 * 执行策略接口
 */
public interface ExecutionStrategy {

    /**
     * 执行规则组
     * @param ruleGroup 规则组
     * @param factContext 事实上下文
     * @return 组执行结果
     */
    GroupResult execute(RuleGroup ruleGroup, FactContext factContext);
}
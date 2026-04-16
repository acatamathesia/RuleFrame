package com.ruleframe.executor.context;

import com.ruleframe.core.rule.RuleResult;

/**
 * 执行过程监听器（用于日志/监控）
 */
public interface ExecutionListener {

    /**
     * 规则执行前调用
     * @param ruleId 规则ID
     * @param context 执行上下文
     */
    void beforeRuleExecute(String ruleId, ExecutionContext context);

    /**
     * 规则执行后调用
     * @param ruleId 规则ID
     * @param result 规则结果
     * @param context 执行上下文
     */
    void afterRuleExecute(String ruleId, RuleResult result, ExecutionContext context);

    /**
     * 规则组执行前调用
     * @param groupKey 规则组键
     * @param context 执行上下文
     */
    void beforeGroupExecute(String groupKey, ExecutionContext context);

    /**
     * 规则组执行后调用
     * @param groupKey 规则组键
     * @param context 执行上下文
     */
    void afterGroupExecute(String groupKey, ExecutionContext context);
}
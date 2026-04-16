package com.ruleframe.executor;

import com.ruleframe.core.fact.FactContext;
import com.ruleframe.core.group.RuleGroup;
import com.ruleframe.config.repository.RuleRepository;
import com.ruleframe.executor.context.ExecutionContext;
import com.ruleframe.executor.result.ExecutionReport;

/**
 * 主要门面：execute(groupKey, facts)
 */
public class RuleExecutor {

    private RuleRepository ruleRepository;

    public RuleExecutor(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    /**
     * 执行规则组
     * @param groupKey 规则组键
     * @param facts 事实上下文
     * @return 执行报告
     */
    public ExecutionReport execute(String groupKey, FactContext facts) {
        // TODO: 实现规则执行逻辑
        return null;
    }

    /**
     * 执行规则组（带执行上下文）
     * @param groupKey 规则组键
     * @param context 执行上下文
     * @return 执行报告
     */
    public ExecutionReport execute(String groupKey, ExecutionContext context) {
        // TODO: 实现带上下文的规则执行逻辑
        return null;
    }
}

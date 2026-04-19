package com.ruleframe.executor;

import com.ruleframe.core.fact.FactContext;
import com.ruleframe.core.group.GroupEvaluator;
import com.ruleframe.core.group.GroupResult;
import com.ruleframe.core.group.RuleGroup;
import com.ruleframe.config.repository.RuleRepository;
import com.ruleframe.executor.context.ExecutionContext;
import com.ruleframe.executor.result.ExecutionReport;

/**
 * 主要门面：execute(groupKey, facts)
 */
public class RuleExecutor {

    private RuleRepository ruleRepository;
    private GroupEvaluator groupEvaluator;

    public RuleExecutor(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
        this.groupEvaluator = new GroupEvaluator();
    }

    /**
     * 执行规则组
     * 
     * @param groupKey 规则组键
     * @param facts    事实上下文
     * @return 执行报告
     */
    public ExecutionReport execute(String groupKey, FactContext facts) {
        long startTime = System.currentTimeMillis();
        try {
            RuleGroup ruleGroup = ruleRepository.getRuleGroup(groupKey);
            GroupResult result = groupEvaluator.evaluate(ruleGroup, facts);
            long duration = System.currentTimeMillis() - startTime;
            if (!result.isAllPassed()) {
                return ExecutionReport.failure(groupKey, duration, "规则组执行失败", result, result.getAllResults());
            }
            return ExecutionReport.success(groupKey, duration, result, result.getAllResults());
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            return ExecutionReport.failure(groupKey, duration, e.getMessage());
        }
    }

    /**
     * 执行规则组（带执行上下文）
     * 
     * @param groupKey 规则组键
     * @param context  执行上下文
     * @return 执行报告
     */
    public ExecutionReport execute(String groupKey, ExecutionContext context) {
        try {
            RuleGroup ruleGroup = ruleRepository.getRuleGroup(groupKey);
            GroupResult result = groupEvaluator.evaluate(ruleGroup, context.getFactContext());
            context.markCompleted();
            if (!result.isAllPassed()) {
                return ExecutionReport.failure(groupKey, context.getDuration(), "规则组执行失败", result, result.getAllResults());
            }
            return ExecutionReport.success(groupKey, context.getDuration(), result, result.getAllResults());
        } catch (Exception e) {
            context.markCompleted();
            return ExecutionReport.failure(groupKey, context.getDuration(), e.getMessage());
        }
    }
}

package com.ruleframe.core.group;

import com.ruleframe.core.fact.FactContext;
import com.ruleframe.core.group.strategy.AllMatchStrategy;
import com.ruleframe.core.group.strategy.ExecutionStrategy;
import com.ruleframe.core.group.strategy.FirstFailStrategy;
import com.ruleframe.core.group.strategy.FirstSuccessStrategy;

/**
 * 根据策略执行规则组的核心逻辑
 */
public class GroupEvaluator {

    /**
     * 执行规则组
     * @param ruleGroup 规则组
     * @param factContext 事实上下文
     * @return 组执行结果
     */
    public GroupResult evaluate(RuleGroup ruleGroup, FactContext factContext) {
        // TODO: 根据规则组的策略选择对应的执行策略
        ExecutionStrategy strategy = getStrategy(ruleGroup.getStrategy());
        return strategy.execute(ruleGroup, factContext);
    }

    /**
     * 根据策略枚举获取对应的策略实现
     * @param strategyEnum 策略枚举
     * @return 策略实现
     */
    private ExecutionStrategy getStrategy(RuleGroup.ExecutionStrategyEnum strategyEnum) {
        // TODO: 实现策略选择逻辑
        if (strategyEnum == null) {
            return new AllMatchStrategy(); // 默认使用全匹配策略
        }

        switch (strategyEnum) {
            case ALL_MATCH:
                return new AllMatchStrategy();
            case FIRST_FAIL:
                return new FirstFailStrategy();
            case FIRST_SUCCESS:
                return new FirstSuccessStrategy();
            default:
                return new AllMatchStrategy();
        }
    }
}
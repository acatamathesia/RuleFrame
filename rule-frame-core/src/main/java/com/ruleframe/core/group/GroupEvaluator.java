package com.ruleframe.core.group;

import java.util.HashMap;
import java.util.Map;

import com.ruleframe.core.fact.FactContext;
import com.ruleframe.core.group.strategy.AllMatchStrategy;
import com.ruleframe.core.group.strategy.ExecutionStrategy;
import com.ruleframe.core.group.strategy.FirstFailStrategy;
import com.ruleframe.core.group.strategy.FirstSuccessStrategy;

/**
 * 根据策略执行规则组的核心逻辑
 */
public class GroupEvaluator {

    private final Map<RuleGroup.ExecutionStrategyEnum, ExecutionStrategy> EXECUTIONSTRATEGY_MAP;

    {
        // 构建对象初始化生成对应的分组策略对应的处理器，避免运行中生成
        EXECUTIONSTRATEGY_MAP = new HashMap<>();
        EXECUTIONSTRATEGY_MAP.put(RuleGroup.ExecutionStrategyEnum.ALL_MATCH, new AllMatchStrategy());
        EXECUTIONSTRATEGY_MAP.put(RuleGroup.ExecutionStrategyEnum.FIRST_FAIL, new FirstFailStrategy());
        EXECUTIONSTRATEGY_MAP.put(RuleGroup.ExecutionStrategyEnum.FIRST_SUCCESS, new FirstSuccessStrategy());
    }

    /**
     * 执行规则组
     * 
     * @param ruleGroup   规则组
     * @param factContext 事实上下文
     * @return 组执行结果
     */
    public GroupResult evaluate(RuleGroup ruleGroup, FactContext factContext) {
        ExecutionStrategy strategy = getStrategy(ruleGroup.getStrategy());
        return strategy.execute(ruleGroup, factContext);
    }

    /**
     * 根据策略枚举获取对应的策略实现
     * 
     * @param strategyEnum 策略枚举
     * @return 策略实现
     */
    private ExecutionStrategy getStrategy(RuleGroup.ExecutionStrategyEnum strategyEnum) {
        if (strategyEnum == null) {
            return EXECUTIONSTRATEGY_MAP.get(RuleGroup.ExecutionStrategyEnum.ALL_MATCH); // 默认使用全匹配策略
        }

        return EXECUTIONSTRATEGY_MAP.getOrDefault(strategyEnum,
                EXECUTIONSTRATEGY_MAP.get(RuleGroup.ExecutionStrategyEnum.ALL_MATCH));
    }
}
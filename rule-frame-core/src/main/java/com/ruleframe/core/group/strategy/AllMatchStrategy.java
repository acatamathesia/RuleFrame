package com.ruleframe.core.group.strategy;

import com.ruleframe.core.group.GroupResult;
import com.ruleframe.core.group.RuleGroup;
import com.ruleframe.core.fact.FactContext;
import com.ruleframe.core.rule.Rule;
import com.ruleframe.core.rule.RuleResult;
import java.util.ArrayList;
import java.util.List;

/**
 * 全匹配策略：所有规则都必须通过
 */
public class AllMatchStrategy implements ExecutionStrategy {

    @Override
    public GroupResult execute(RuleGroup ruleGroup, FactContext factContext) {
        // TODO: 实现全匹配策略逻辑
        List<RuleResult> allResults = new ArrayList<>();
        List<RuleResult> failedRules = new ArrayList<>();

        for (Rule rule : ruleGroup.getRules()) {
            // TODO: 执行规则并收集结果
        }

        boolean allPassed = failedRules.isEmpty();
        return new GroupResult(allPassed, failedRules, allResults);
    }
}
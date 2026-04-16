package com.ruleframe.core.group.strategy;

import com.ruleframe.core.group.GroupResult;
import com.ruleframe.core.group.RuleGroup;
import com.ruleframe.core.fact.FactContext;
import com.ruleframe.core.rule.Rule;
import com.ruleframe.core.rule.RuleResult;
import java.util.ArrayList;
import java.util.List;

/**
 * 首次失败策略：遇到第一个失败的规则就停止执行
 */
public class FirstFailStrategy implements ExecutionStrategy {

    @Override
    public GroupResult execute(RuleGroup ruleGroup, FactContext factContext) {
        // TODO: 实现首次失败策略逻辑
        List<RuleResult> allResults = new ArrayList<>();
        List<RuleResult> failedRules = new ArrayList<>();

        for (Rule rule : ruleGroup.getRules()) {
            // TODO: 执行规则，如果失败则立即返回
        }

        boolean allPassed = failedRules.isEmpty();
        return new GroupResult(allPassed, failedRules, allResults);
    }
}
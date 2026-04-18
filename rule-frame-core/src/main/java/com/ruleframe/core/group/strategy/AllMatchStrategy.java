package com.ruleframe.core.group.strategy;

import com.ruleframe.core.group.GroupResult;
import com.ruleframe.core.group.RuleGroup;
import com.ruleframe.core.condition.ConditionResult;
import com.ruleframe.core.fact.FactContext;
import com.ruleframe.core.rule.Rule;
import com.ruleframe.core.rule.RuleResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 全匹配策略：所有规则都必须通过
 */
public class AllMatchStrategy implements ExecutionStrategy {

    @Override
    public GroupResult execute(RuleGroup ruleGroup, FactContext factContext) {
        List<RuleResult> allResults = new ArrayList<>();
        List<RuleResult> failedRules = new ArrayList<>();

        for (Rule rule : ruleGroup.getRules()) {
            RuleResult result = rule.execute(factContext);
            allResults.add(result);
            if (!result.isPassed()) {
                failedRules.add(result);
            }
        }

        boolean allPassed = failedRules.isEmpty();
        return new GroupResult(allPassed, failedRules, allResults);
    }
}
package com.ruleframe.core.rule;

import java.util.List;

import com.ruleframe.core.condition.Condition;
import com.ruleframe.core.condition.ConditionResult;
import com.ruleframe.core.fact.FactContext;

import lombok.Builder;
import lombok.Data;

/**
 * 规则
 * 
 */
@Data
@Builder
public class Rule {

    private String id; // 唯一id
    private String name; // 规则名称
    private Integer priority; // 优先级
    private List<Condition> conditionList; // 规则内包含的条件
    private boolean unifiedReturn; // 是否统一返回，如果统一返回，那么就会按照规则配置的结果返回
    private RuleResult result;

    public RuleResult execute(FactContext ctx) {
        List<ConditionResult> conditionResultList = conditionList.stream()
                .map(m -> m.evaluate(ctx))
                .toList();

        boolean allPassed = conditionResultList.stream().allMatch(ConditionResult::isPassed);
        result.setPassed(allPassed);
        if (!unifiedReturn) {
            // 非统一返回模式：需要收集失败原因
            List<String> failureReasonList = conditionResultList.stream()
                    .filter(f -> !f.isPassed())
                    .map(ConditionResult::getFailureReason)
                    .toList();
            if (!allPassed) {
                result.setFailureResult(String.join(";", failureReasonList));
            }
        }
        return result;
    }

}

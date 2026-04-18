package com.ruleframe.core.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.ruleframe.core.fact.FactContext;

import lombok.Data;

@Data
public class CompositeCondition implements Condition {

    private final LogicalOperator logicalOperator;
    private final List<Condition> conditions;
    private final EvaluationMode evaluationMode;

    @Override
    public ConditionResult evaluate(FactContext ctx) {
        if (logicalOperator == LogicalOperator.AND) {
            return evaluateAnd(ctx);
        }
        return evaluateOr(ctx);
    }

    private ConditionResult evaluateAnd(FactContext ctx) {
        List<ConditionResult> failureResultList = new ArrayList<>();

        for (Condition itemCondition : conditions) {
            ConditionResult result = itemCondition.evaluate(ctx);
            if (!result.isPassed()) {
                failureResultList.add(result);
                if (evaluationMode == EvaluationMode.SHORT_CIRCUIT) {
                    break;
                }
            }
        }

        if (failureResultList.isEmpty()) {
            // 所有条件都通过才符合要求
            return ConditionResult.success();
        }
        return ConditionResult.failure(failureResultList.stream()
                .map(ConditionResult::getFailureReason).collect(Collectors.joining(";")));
    }

    /**
     * 所有参与的条件，只要有一个符合要求，那么这个条件判断就是正确的
     * 
     * @param ctx 数据上下文
     * @return 条件判断结果
     */
    private ConditionResult evaluateOr(FactContext ctx) {
        List<ConditionResult> failureResultList = new ArrayList<>();
        for (Condition itemCondition : conditions) {
            ConditionResult result = itemCondition.evaluate(ctx);
            if (result.isPassed()) {
                return result;
            } else {
                failureResultList.add(result);
            }
        }
        return ConditionResult.failure(failureResultList.stream()
                .map(ConditionResult::getFailureReason).collect(Collectors.joining(";")));
    }

}

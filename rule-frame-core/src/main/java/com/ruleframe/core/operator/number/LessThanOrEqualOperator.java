package com.ruleframe.core.operator.number;

import java.math.BigDecimal;
import java.util.List;

import com.ruleframe.core.operator.AutoRegisterOperator;

/**
 * 小于等于操作符 (<=)
 */
@AutoRegisterOperator("<=")
public class LessThanOrEqualOperator extends NumberOperator {

    @Override
    protected boolean simpleApply(BigDecimal factValue, BigDecimal expectedValue) {
        return factValue.compareTo(expectedValue) <= 0;
    }

    @Override
    protected boolean listApply(List<BigDecimal> factValues, BigDecimal expectedValue) {
        return factValues.stream()
                .allMatch(v -> v.compareTo(expectedValue) <= 0);
    }
}

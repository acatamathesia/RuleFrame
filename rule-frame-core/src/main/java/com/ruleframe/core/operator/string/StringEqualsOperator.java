package com.ruleframe.core.operator.string;

import java.util.List;
import java.util.Objects;

import com.ruleframe.core.operator.AutoRegisterOperator;
import com.ruleframe.core.operator.number.NumberOperator;

/**
 * StringEquals操作符 (EQUALS)
 * 比较两个值是否相等，支持字符串、数字、布尔值的比较
 */
@AutoRegisterOperator("EQUALS")
public class StringEqualsOperator extends NumberOperator {

    @Override
    protected boolean simpleApply(java.math.BigDecimal factValue, java.math.BigDecimal expectedValue) {
        return factValue.compareTo(expectedValue) == 0;
    }

    @Override
    protected boolean listApply(List<java.math.BigDecimal> factValues, java.math.BigDecimal expectedValue) {
        return factValues.stream().allMatch(v -> v.compareTo(expectedValue) == 0);
    }

    @Override
    public boolean apply(Object factValue, Object expectedValue) {
        String factStr = normalizeValue(factValue);
        String expectedStr = normalizeValue(expectedValue);
        return Objects.equals(factStr, expectedStr);
    }

    private String normalizeValue(Object value) {
        if (value == null) return null;
        return value.toString().trim();
    }
}

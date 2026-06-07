package com.ruleframe.core.operator.string;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.ruleframe.core.operator.AutoRegisterOperator;
import com.ruleframe.core.operator.number.NumberOperator;

/**
 * IsTrue操作符 (IS_TRUE)
 * 检查事实值是否为布尔true（字符串"true"不区分大小写）
 */
@AutoRegisterOperator("IS_TRUE")
public class IsTrueOperator extends NumberOperator {

    @Override
    protected boolean simpleApply(BigDecimal factValue, BigDecimal expectedValue) {
        return false;
    }

    @Override
    protected boolean listApply(List<BigDecimal> factValues, BigDecimal expectedValue) {
        return false;
    }

    @Override
    public boolean apply(Object factValue, Object expectedValue) {
        return isTruthy(factValue);
    }

    private boolean isTruthy(Object value) {
        if (value == null) return false;
        String str = value.toString().trim().toLowerCase();
        return "true".equals(str) || "1".equals(str) || "yes".equals(str);
    }
}

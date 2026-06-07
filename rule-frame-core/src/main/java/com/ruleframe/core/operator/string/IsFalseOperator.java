package com.ruleframe.core.operator.string;

import java.util.List;

import com.ruleframe.core.operator.AutoRegisterOperator;
import com.ruleframe.core.operator.number.NumberOperator;

/**
 * IsFalse操作符 (IS_FALSE)
 * 检查事实值是否为布尔false（字符串"false"不区分大小写）
 */
@AutoRegisterOperator("IS_FALSE")
public class IsFalseOperator extends NumberOperator {

    @Override
    protected boolean simpleApply(java.math.BigDecimal factValue, java.math.BigDecimal expectedValue) {
        return false;
    }

    @Override
    protected boolean listApply(List<java.math.BigDecimal> factValues, java.math.BigDecimal expectedValue) {
        return false;
    }

    @Override
    public boolean apply(Object factValue, Object expectedValue) {
        if (factValue == null) return true;
        String str = factValue.toString().trim().toLowerCase();
        return "false".equals(str) || "0".equals(str) || "no".equals(str);
    }
}

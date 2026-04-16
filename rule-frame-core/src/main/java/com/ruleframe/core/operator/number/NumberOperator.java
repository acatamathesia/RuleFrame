package com.ruleframe.core.operator.number;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import com.ruleframe.core.operator.Operator;

public abstract class NumberOperator implements Operator {

    @Override
    public boolean apply(Object factValue, Object expectedValue) {
        try {
            if (factValue instanceof List) {
                List<?> list = (List<?>) factValue;
                List<BigDecimal> numbers = new ArrayList<>();
                for (Object item : list) {
                    BigDecimal num = toBigDecimal(item);
                    if (num == null) {
                        return false;
                    }
                    numbers.add(num);
                }
                BigDecimal expected = toBigDecimal(expectedValue);
                if (expected == null) {
                    return false;
                }
                return listApply(numbers, expected);
            }
            
            BigDecimal factNum = toBigDecimal(factValue);
            if (factNum == null) {
                return false;
            }
            BigDecimal expected = toBigDecimal(expectedValue);
            if (expected == null) {
                return false;
            }
            return simpleApply(factNum, expected);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将Object转换为BigDecimal类型
     * @param value 待转换的值
     * @return 转换后的BigDecimal，如果无法转换则返回null
     */
    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return new BigDecimal(value.toString());
        }
        if (value instanceof String) {
            try {
                return new BigDecimal(((String) value).trim());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    protected abstract boolean listApply(List<BigDecimal> factValues, BigDecimal expectedValue);

    protected abstract boolean simpleApply(BigDecimal factValue, BigDecimal expectedValue);
    
}
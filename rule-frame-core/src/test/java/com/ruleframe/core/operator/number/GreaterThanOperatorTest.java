package com.ruleframe.core.operator.number;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

/**
 * GreaterThanOperator 单元测试 测试大于运算符的标量比较和列表比较
 */
public class GreaterThanOperatorTest {

    private final GreaterThanOperator operator = new GreaterThanOperator();

    @Test
    public void testGreaterThanTrue() {
        assertTrue(operator.apply(10, 5), "10 > 5 应为true");
    }

    @Test
    public void testGreaterThanFalse() {
        assertFalse(operator.apply(5, 10), "5 > 10 应为false");
    }

    @Test
    public void testEqualToNotGreater() {
        assertFalse(operator.apply(5, 5), "5 > 5 应为false（等于不算大于）");
    }

    @Test
    public void testDecimalGreaterThan() {
        assertTrue(operator.apply(3.15, 3.14), "3.15 > 3.14 应为true");
    }

    @Test
    public void testStringNumericComparison() {
        assertTrue(operator.apply("200", "100"), "字符串'200' > '100' 应为true");
    }

    @Test
    public void testListAllGreaterThan() {
        assertTrue(operator.apply(Arrays.asList(20, 30, 40), 10),
                "列表所有元素都大于10时应返回true");
    }

    @Test
    public void testListNotAllGreaterThan() {
        assertFalse(operator.apply(Arrays.asList(5, 20, 30), 10),
                "列表中有一个元素不大于10时应返回false");
    }

    @Test
    public void testNullFactValue() {
        assertFalse(operator.apply(null, 10), "null比较应返回false");
    }

    @Test
    public void testNullExpectedValue() {
        assertFalse(operator.apply(10, null), "与null比较应返回false");
    }
}

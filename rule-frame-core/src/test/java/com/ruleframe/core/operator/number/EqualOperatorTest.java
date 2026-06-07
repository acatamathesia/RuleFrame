package com.ruleframe.core.operator.number;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

/**
 * EqualOperator 单元测试 测试等于运算符的标量比较和列表比较
 */
public class EqualOperatorTest {

    private final EqualOperator operator = new EqualOperator();

    @Test
    public void testEqualIntegers() {
        assertTrue(operator.apply(42, 42), "42 = 42 应为true");
    }

    @Test
    public void testNotEqualIntegers() {
        assertFalse(operator.apply(42, 43), "42 != 43 应为false");
    }

    @Test
    public void testEqualDecimals() {
        assertTrue(operator.apply(3.14, 3.14), "3.14 = 3.14 应为true");
    }

    @Test
    public void testEqualStringsNumeric() {
        assertTrue(operator.apply("100", "100"), "字符串'100' = '100' 应为true");
    }

    @Test
    public void testEqualLong() {
        assertTrue(operator.apply(100L, 100L), "两个long型相等");
    }

    @Test
    public void testCrossTypeEquality() {
        // Integer和Long同样数值应相等
        assertTrue(operator.apply(100, 100L), "相同数值的不同数字类型应视为相等");
    }

    @Test
    public void testListAllEqual() {
        assertTrue(operator.apply(Arrays.asList(10, 10, 10), 10),
                "列表所有元素都等于10时应返回true");
    }

    @Test
    public void testListNotAllEqual() {
        assertFalse(operator.apply(Arrays.asList(10, 20, 10), 10),
                "列表中有一个元素不等于10时应返回false");
    }

    @Test
    public void testEmptyList() {
        // 空列表: allMatch在空流上返回true
        assertTrue(operator.apply(Collections.emptyList(), 10),
                "空列表的allMatch应返回true");
    }

    @Test
    public void testNullFactValue() {
        assertFalse(operator.apply(null, 10), "factValue为null应返回false");
    }

    @Test
    public void testNullExpectedValue() {
        assertFalse(operator.apply(10, null), "expectedValue为null应返回false");
    }

    @Test
    public void testNonNumericFactValue() {
        assertFalse(operator.apply("not_a_number", 10), "非数字字符串应返回false");
    }

    @Test
    public void testNonNumericExpectedValue() {
        assertFalse(operator.apply(10, "not_a_number"), "非数字期望值应返回false");
    }

    @Test
    public void testListWithNonNumericItem() {
        assertFalse(operator.apply(Arrays.asList(10, "bad", 10), 10),
                "列表中包含非数字元素应返回false");
    }
}

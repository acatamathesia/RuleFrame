package com.ruleframe.core.condition;

import com.ruleframe.core.element.Element;
import com.ruleframe.core.element.ElementValue;
import com.ruleframe.core.fact.FactContext;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

/**
 * LeafCondition 单元测试 测试简单条件（单值比较）和复杂条件（从上下文提取值比较）
 */
public class LeafConditionTest {

    /**
     * 模拟 Element：总是返回固定值
     */
    private static Element fixedElement(String name, Object rawValue, Object convertedValue) {
        return new Element() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public ElementValue resolve(FactContext context) {
                return ElementValue.success(rawValue, convertedValue,
                        convertedValue != null ? convertedValue.getClass() : null);
            }
        };
    }

    /**
     * 模拟成功的 FactContext
     */
    private static FactContext emptyContext() {
        return new FactContext() {
            @Override
            public Object getValue(String name) {
                return null;
            }

            @Override
            public Set<String> getFactNames() {
                return Set.of();
            }

            @Override
            public boolean hasFact(String name) {
                return false;
            }
        };
    }

    /** 始终返回 true 的简单运算符 */
    private static final com.ruleframe.core.operator.Operator alwaysTrue = (fact, expected) -> true;

    /** 始终返回 false 的简单运算符 */
    private static final com.ruleframe.core.operator.Operator alwaysFalse = (fact, expected) -> false;

    @Test
    public void testSimpleConditionPass() {
        Element elem = fixedElement("age", 25, 25);
        LeafCondition condition = LeafCondition.builder()
                .element(elem)
                .operator(alwaysTrue)
                .simple(true)
                .expectedValue(25)
                .failureCode("ERR001")
                .failureMessage("年龄应为{expectedValue}，实际为{factValue}")
                .build();

        ConditionResult result = condition.evaluate(emptyContext());
        assertTrue(result.isPassed(), "条件满足时应通过");
    }

    @Test
    public void testSimpleConditionFail() {
        Element elem = fixedElement("age", 25, 25);
        LeafCondition condition = LeafCondition.builder()
                .element(elem)
                .operator(alwaysFalse)
                .simple(true)
                .expectedValue(30)
                .failureCode("ERR001")
                .failureMessage("年龄应为{expectedValue}，实际为{factValue}")
                .build();

        ConditionResult result = condition.evaluate(emptyContext());
        assertFalse(result.isPassed(), "条件不满足时应失败");
        assertNotNull(result.getFailureReason(), "失败时应有失败原因");
        assertTrue(result.getFailureReason().contains("ERR001"), "失败原因应包含错误码");
    }

    @Test
    public void testMissingElementThrows() {
        LeafCondition condition = LeafCondition.builder()
                .element(null)
                .operator(alwaysTrue)
                .simple(true)
                .expectedValue(1)
                .build();

        ConditionResult result = condition.evaluate(emptyContext());
        assertFalse(result.isPassed(), "缺少element时应失败");
        assertTrue(result.getFailureReason().contains("元素(element)不存在"));
    }

    @Test
    public void testMissingOperatorThrows() {
        Element elem = fixedElement("age", 25, 25);
        LeafCondition condition = LeafCondition.builder()
                .element(elem)
                .operator(null)
                .simple(true)
                .expectedValue(25)
                .build();

        ConditionResult result = condition.evaluate(emptyContext());
        assertFalse(result.isPassed(), "缺少operator时应失败");
        assertTrue(result.getFailureReason().contains("运算器(operator)不存在"));
    }

    @Test
    public void testMissingExpectedValue() {
        Element elem = fixedElement("age", 25, 25);
        LeafCondition condition = LeafCondition.builder()
                .element(elem)
                .operator(alwaysTrue)
                .simple(true)
                .expectedValue(null)
                .build();

        ConditionResult result = condition.evaluate(emptyContext());
        assertFalse(result.isPassed(), "缺少expectedValue时应失败");
        assertTrue(result.getFailureReason().contains("预期值(expectedValue)不存在"));
    }

    @Test
    public void testNonSimpleConditionUsesExpectedElement() {
        // 非简单模式：预期值从 expectedElement 解析
        Element factElem = fixedElement("status", "ACTIVE", "ACTIVE");
        Element expectedElem = fixedElement("expectedStatus", "ACTIVE", "ACTIVE");

        LeafCondition condition = LeafCondition.builder()
                .element(factElem)
                .operator(alwaysTrue)
                .simple(false)
                .expectedElement(expectedElem)
                .failureCode("ERR002")
                .failureMessage("状态不匹配")
                .build();

        ConditionResult result = condition.evaluate(emptyContext());
        assertTrue(result.isPassed(), "非简单模式：expectedElement解析值匹配时应通过");
    }

    @Test
    public void testFailureMessageTemplateReplacement() {
        Element elem = fixedElement("score", 60, 60);
        LeafCondition condition = LeafCondition.builder()
                .element(elem)
                .operator(alwaysFalse)
                .simple(true)
                .expectedValue(80)
                .failureCode("SCORE_LOW")
                .failureMessage("分数{factValue}低于要求{expectedValue}")
                .build();

        ConditionResult result = condition.evaluate(emptyContext());
        assertFalse(result.isPassed());
        assertTrue(result.getFailureReason().contains("SCORE_LOW"));
        assertTrue(result.getFailureReason().contains("60"), "失败信息应包含实际值 60");
        assertTrue(result.getFailureReason().contains("80"), "失败信息应包含期望值 80");
    }

    @Test
    public void testConditionVisitorAccept() {
        Element elem = fixedElement("x", 1, 1);
        LeafCondition condition = LeafCondition.builder()
                .element(elem)
                .operator(alwaysTrue)
                .simple(true)
                .expectedValue(1)
                .build();

        String result = condition.accept(new com.ruleframe.core.condition.visitor.ConditionVisitor<String>() {
            @Override
            public String visit(LeafCondition lc) {
                return "visited leaf: " + lc.getElement().getName();
            }

            @Override
            public String visit(CompositeCondition cc) {
                return "composite";
            }
        });
        assertEquals("visited leaf: x", result);
    }
}

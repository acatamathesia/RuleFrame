package com.ruleframe.core.rule;

import com.ruleframe.core.condition.Condition;
import com.ruleframe.core.condition.ConditionResult;
import com.ruleframe.core.fact.FactContext;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

/**
 * Rule 单元测试 测试规则执行（统一返回模式和非统一返回模式）
 */
public class RuleTest {

    /** 始终通过的条件 */
    private static Condition passCondition() {
        return new Condition() {
            @Override
            public ConditionResult evaluate(FactContext ctx) {
                return ConditionResult.success();
            }

            @Override
            public <T> T accept(com.ruleframe.core.condition.visitor.ConditionVisitor<T> visitor) {
                throw new UnsupportedOperationException();
            }
        };
    }

    /** 始终失败的条件 */
    private static Condition failCondition(String reason) {
        return new Condition() {
            @Override
            public ConditionResult evaluate(FactContext ctx) {
                return ConditionResult.failure(reason);
            }

            @Override
            public <T> T accept(com.ruleframe.core.condition.visitor.ConditionVisitor<T> visitor) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void testAllConditionsPass() {
        RuleResult expectedResult = RuleResult.builder()
                .ruleId("R001")
                .ruleName("测试规则")
                .isPassed(false)
                .build();

        Rule rule = Rule.builder()
                .id("R001")
                .name("测试规则")
                .priority(1)
                .conditionList(Arrays.asList(passCondition(), passCondition()))
                .unifiedReturn(false)
                .result(expectedResult)
                .build();

        RuleResult result = rule.execute(null);
        assertTrue(result.isPassed(), "所有条件通过时应为passed=true");
        assertEquals("R001", result.getRuleId());
        assertEquals("测试规则", result.getRuleName());
    }

    @Test
    public void testOneConditionFails() {
        RuleResult expectedResult = RuleResult.builder()
                .ruleId("R002")
                .ruleName("失败规则")
                .isPassed(true)
                .build();

        Rule rule = Rule.builder()
                .id("R002")
                .name("失败规则")
                .priority(2)
                .conditionList(Arrays.asList(passCondition(), failCondition("条件不满足"), passCondition()))
                .unifiedReturn(false)
                .result(expectedResult)
                .build();

        RuleResult result = rule.execute(null);
        assertFalse(result.isPassed(), "任一条件失败时应为passed=false");
        assertNotNull(result.getFailureResult(), "非统一返回模式下应有失败信息");
    }

    @Test
    public void testMultipleFailureReasons() {
        RuleResult expectedResult = RuleResult.builder()
                .ruleId("R003")
                .ruleName("多失败")
                .isPassed(true)
                .build();

        Rule rule = Rule.builder()
                .id("R003")
                .name("多失败")
                .priority(3)
                .conditionList(Arrays.asList(
                        failCondition("失败原因A"),
                        failCondition("失败原因B")))
                .unifiedReturn(false)
                .result(expectedResult)
                .build();

        RuleResult result = rule.execute(null);
        assertFalse(result.isPassed());
        String failureReason = result.getFailureResult();
        assertNotNull(failureReason);
        assertTrue(failureReason.contains("失败原因A"), "应收集失败原因A");
        assertTrue(failureReason.contains("失败原因B"), "应收集失败原因B");
        assertTrue(failureReason.contains(";"), "多个失败原因应以分号分隔");
    }

    @Test
    public void testUnifiedReturnMode() {
        // 统一返回模式：不收集失败原因
        RuleResult expectedResult = RuleResult.builder()
                .ruleId("R004")
                .ruleName("统一返回")
                .isPassed(true)
                .failureResult("预置错误信息")
                .build();

        Rule rule = Rule.builder()
                .id("R004")
                .name("统一返回")
                .priority(4)
                .conditionList(Arrays.asList(failCondition("忽略的失败")))
                .unifiedReturn(true)
                .result(expectedResult)
                .build();

        RuleResult result = rule.execute(null);
        assertFalse(result.isPassed());
        // 统一返回模式：result 应该是原 RuleResult 对象（isPassed 被修改为 false）
        assertEquals("R004", result.getRuleId());
        assertEquals("统一返回", result.getRuleName());
    }

    @Test
    public void testEmptyConditionList() {
        RuleResult expectedResult = RuleResult.builder()
                .ruleId("R005")
                .ruleName("空条件")
                .isPassed(false)
                .build();

        Rule rule = Rule.builder()
                .id("R005")
                .name("空条件")
                .priority(5)
                .conditionList(Collections.emptyList())
                .unifiedReturn(false)
                .result(expectedResult)
                .build();

        RuleResult result = rule.execute(null);
        // 空条件列表: stream 为空 -> allMatch 返回 true
        assertTrue(result.isPassed(), "空条件列表应视为所有条件通过");
    }

    @Test
    public void testBuilderPattern() {
        Rule rule = Rule.builder()
                .id("B001")
                .name("构建器测试")
                .priority(10)
                .conditionList(Collections.emptyList())
                .unifiedReturn(true)
                .result(RuleResult.builder()
                        .ruleId("B001")
                        .ruleName("构建器测试")
                        .build())
                .build();

        assertNotNull(rule);
        assertEquals("B001", rule.getId());
        assertEquals("构建器测试", rule.getName());
        assertEquals(10, rule.getPriority());
    }
}

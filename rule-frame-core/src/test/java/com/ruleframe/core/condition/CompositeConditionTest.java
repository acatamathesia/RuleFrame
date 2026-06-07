package com.ruleframe.core.condition;

import com.ruleframe.core.element.Element;
import com.ruleframe.core.element.ElementValue;
import com.ruleframe.core.fact.FactContext;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * CompositeCondition 单元测试 测试复合条件（AND/OR）和评估模式（SHORT_CIRCUIT/COLLECT_ALL）
 */
public class CompositeConditionTest {

    /** 始终通过的条件 */
    private static Condition passCondition() {
        return new Condition() {
            @Override
            public ConditionResult evaluate(FactContext ctx) {
                return ConditionResult.success();
            }

            @Override
            public <T> T accept(com.ruleframe.core.condition.visitor.ConditionVisitor<T> visitor) {
                throw new UnsupportedOperationException("not implemented");
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
                throw new UnsupportedOperationException("not implemented");
            }
        };
    }

    /** 抛出异常的条件 */
    private static Condition errorCondition() {
        return new Condition() {
            @Override
            public ConditionResult evaluate(FactContext ctx) {
                throw new RuntimeException("测试异常");
            }

            @Override
            public <T> T accept(com.ruleframe.core.condition.visitor.ConditionVisitor<T> visitor) {
                throw new UnsupportedOperationException("not implemented");
            }
        };
    }

    // ---------- AND 逻辑 ----------

    @Test
    public void testAndAllPass() {
        CompositeCondition cond = new CompositeCondition(
                LogicalOperator.AND,
                Arrays.asList(passCondition(), passCondition(), passCondition()),
                EvaluationMode.COLLECT_ALL);

        ConditionResult result = cond.evaluate(null);
        assertTrue(result.isPassed(), "所有条件都通过时 AND 应返回 true");
    }

    @Test
    public void testAndOneFail() {
        CompositeCondition cond = new CompositeCondition(
                LogicalOperator.AND,
                Arrays.asList(passCondition(), failCondition("条件2失败"), passCondition()),
                EvaluationMode.COLLECT_ALL);

        ConditionResult result = cond.evaluate(null);
        assertFalse(result.isPassed(), "任一条件失败时 AND 应返回 false");
        assertTrue(result.getFailureReason().contains("条件2失败"), "失败原因应包含具体失败信息");
    }

    @Test
    public void testAndShortCircuit() {
        // SHORT_CIRCUIT 模式：第一个失败后停止评估
        CompositeCondition cond = new CompositeCondition(
                LogicalOperator.AND,
                Arrays.asList(passCondition(), failCondition("失败1"), errorCondition()),
                EvaluationMode.SHORT_CIRCUIT);

        ConditionResult result = cond.evaluate(null);
        assertFalse(result.isPassed(), "SHORT_CIRCUIT模式第一个失败后应停止");
        assertTrue(result.getFailureReason().contains("失败1"), "应只报告第一个失败");
        // 后续条件不应被评估（如果第三个条件被评估，会抛异常）
    }

    @Test
    public void testAndCollectAllFailures() {
        // COLLECT_ALL 模式：收集所有失败原因
        CompositeCondition cond = new CompositeCondition(
                LogicalOperator.AND,
                Arrays.asList(failCondition("失败1"), passCondition(), failCondition("失败2")),
                EvaluationMode.COLLECT_ALL);

        ConditionResult result = cond.evaluate(null);
        assertFalse(result.isPassed());
        String reason = result.getFailureReason();
        assertTrue(reason.contains("失败1"), "应收集失败1");
        assertTrue(reason.contains("失败2"), "应收集失败2");
        // 失败原因以 ";" 分隔
        assertTrue(reason.contains(";"));
    }

    // ---------- OR 逻辑 ----------

    @Test
    public void testOrAllFail() {
        CompositeCondition cond = new CompositeCondition(
                LogicalOperator.OR,
                Arrays.asList(failCondition("失败1"), failCondition("失败2"), failCondition("失败3")),
                EvaluationMode.COLLECT_ALL);

        ConditionResult result = cond.evaluate(null);
        assertFalse(result.isPassed(), "所有条件都失败时 OR 应返回 false");
        String reason = result.getFailureReason();
        assertTrue(reason.contains("失败1"));
        assertTrue(reason.contains("失败2"));
        assertTrue(reason.contains("失败3"));
    }

    @Test
    public void testOrFirstPassWins() {
        CompositeCondition cond = new CompositeCondition(
                LogicalOperator.OR,
                Arrays.asList(failCondition("失败1"), passCondition(), failCondition("失败2")),
                EvaluationMode.COLLECT_ALL);

        ConditionResult result = cond.evaluate(null);
        assertTrue(result.isPassed(), "任一条件通过时 OR 应返回 true");
    }

    @Test
    public void testOrEmptyConditions() {
        CompositeCondition cond = new CompositeCondition(
                LogicalOperator.OR,
                Collections.emptyList(),
                EvaluationMode.SHORT_CIRCUIT);

        ConditionResult result = cond.evaluate(null);
        // 空条件列表: OR 遍历完所有条件但都失败 -> 返回包含空列表的失败信息
        assertFalse(result.isPassed(), "空条件列表应返回失败");
    }

    @Test
    public void testAndEmptyConditions() {
        CompositeCondition cond = new CompositeCondition(
                LogicalOperator.AND,
                Collections.emptyList(),
                EvaluationMode.COLLECT_ALL);

        ConditionResult result = cond.evaluate(null);
        // 空条件列表: AND 的 failureResultList 为空 -> 返回成功
        assertTrue(result.isPassed(), "空AND条件列表：所有条件（0个）都满足，应返回成功");
    }

    // ---------- Visitor 模式 ----------

    @Test
    public void testVisitorAccept() {
        CompositeCondition cond = new CompositeCondition(
                LogicalOperator.AND,
                Arrays.asList(passCondition(), passCondition()),
                EvaluationMode.COLLECT_ALL);

        String result = cond.accept(new com.ruleframe.core.condition.visitor.ConditionVisitor<String>() {
            @Override
            public String visit(CompositeCondition c) {
                return "visited with " + c.getConditions().size() + " children";
            }

            @Override
            public String visit(LeafCondition lc) {
                return "leaf";
            }
        });
        assertEquals("visited with 2 children", result);
    }
}

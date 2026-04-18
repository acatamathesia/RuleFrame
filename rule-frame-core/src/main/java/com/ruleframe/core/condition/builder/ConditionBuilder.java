package com.ruleframe.core.condition.builder;

import com.ruleframe.core.condition.CompositeCondition;
import com.ruleframe.core.condition.Condition;
import com.ruleframe.core.condition.ConditionResult;
import com.ruleframe.core.condition.EvaluationMode;
import com.ruleframe.core.condition.LeafCondition;
import com.ruleframe.core.condition.LogicalOperator;
import com.ruleframe.core.element.ConfigurableElement;
import com.ruleframe.core.element.Element;
import com.ruleframe.core.fact.FactContext;
import com.ruleframe.core.fact.MapFactContext;
import com.ruleframe.core.operator.OperatorRegistry;
import com.ruleframe.resolver.MapPathResolver;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 条件构建器，支持构建复杂的嵌套条件表达式
 * 
 * <p>
 * 使用示例：
 * 
 * <pre>
 * // 简单示例：条件1 AND 条件2 AND 条件3
 * ConditionBuilder.create()
 *         .and(condition1)
 *         .and(condition2)
 *         .and(condition3)
 *         .build();
 * 
 * // 嵌套示例：条件1 AND (条件2 OR 条件3)
 * ConditionBuilder.create()
 *         .and(condition1)
 *         .andGroup(ConditionBuilder.create()
 *                 .or(condition2)
 *                 .or(condition3))
 *         .build();
 * 
 * // 复杂示例：(条件1 OR 条件2) AND (条件3 OR 条件4)
 * ConditionBuilder.create()
 *         .andGroup(ConditionBuilder.create()
 *                 .or(condition1)
 *                 .or(condition2))
 *         .andGroup(ConditionBuilder.create()
 *                 .or(condition3)
 *                 .or(condition4))
 *         .build();
 * </pre>
 */
@NoArgsConstructor
public class ConditionBuilder {

    private final List<ConditionNode> nodes = new ArrayList<>();
    private EvaluationMode evaluationMode = EvaluationMode.SHORT_CIRCUIT;

    /**
     * 条件节点，包含条件和连接操作符
     */
    private static class ConditionNode {
        LogicalOperator operator; // 与前一个条件的连接操作符（第一个节点为 null）
        Condition condition;

        ConditionNode(LogicalOperator operator, Condition condition) {
            this.operator = operator;
            this.condition = condition;
        }
    }

    /**
     * 创建一个新的条件构建器
     */
    public static ConditionBuilder create() {
        return new ConditionBuilder();
    }

    /**
     * 设置评估模式
     *
     * @param mode 评估模式
     * @return 当前构建器实例
     */
    public ConditionBuilder evaluationMode(EvaluationMode mode) {
        this.evaluationMode = mode;
        return this;
    }

    /**
     * 添加第一个条件（作为起始条件）
     *
     * @param condition 条件
     * @return 当前构建器实例
     */
    public ConditionBuilder with(Condition condition) {
        if (!nodes.isEmpty()) {
            throw new IllegalStateException("with() 只能用于添加第一个条件，后续条件请使用 and() 或 or()");
        }
        nodes.add(new ConditionNode(null, condition));
        return this;
    }

    /**
     * 添加一个条件，并使用 AND 逻辑连接
     *
     * @param condition 条件
     * @return 当前构建器实例
     */
    public ConditionBuilder and(Condition condition) {
        nodes.add(new ConditionNode(LogicalOperator.AND, condition));
        return this;
    }

    /**
     * 添加一个条件，并使用 OR 逻辑连接
     *
     * @param condition 条件
     * @return 当前构建器实例
     */
    public ConditionBuilder or(Condition condition) {
        nodes.add(new ConditionNode(LogicalOperator.OR, condition));
        return this;
    }

    /**
     * 添加一个嵌套的条件组（子构建器），使用 AND 连接
     * 用于构建如 (条件2 OR 条件3) 这样的分组
     *
     * @param groupBuilder 子条件构建器
     * @return 当前构建器实例
     */
    public ConditionBuilder andGroup(ConditionBuilder groupBuilder) {
        Condition groupCondition = groupBuilder.build();
        return and(groupCondition);
    }

    /**
     * 添加一个嵌套的条件组（子构建器），使用 OR 连接
     *
     * @param groupBuilder 子条件构建器
     * @return 当前构建器实例
     */
    public ConditionBuilder orGroup(ConditionBuilder groupBuilder) {
        Condition groupCondition = groupBuilder.build();
        return or(groupCondition);
    }

    /**
     * 构建最终的条件
     *
     * @return 构建的条件
     * @throws IllegalStateException 如果没有添加任何条件
     */
    public Condition build() {
        if (nodes.isEmpty()) {
            throw new IllegalStateException("至少需要添加一个条件，使用 with() 添加第一个条件");
        }

        if (nodes.size() == 1) {
            return nodes.get(0).condition;
        }

        // 构建复合条件
        return buildComposite();
    }

    /**
     * 构建复合条件
     * 支持 AND 优先级高于 OR 的逻辑处理
     */
    private Condition buildComposite() {
        // 第一遍：处理所有 AND 连接的条件组
        List<Condition> orOperands = new ArrayList<>();
        List<Condition> currentAndGroup = new ArrayList<>();

        currentAndGroup.add(nodes.get(0).condition);

        for (int i = 1; i < nodes.size(); i++) {
            ConditionNode node = nodes.get(i);

            if (node.operator == LogicalOperator.AND) {
                // 继续添加到 AND 组
                currentAndGroup.add(node.condition);
            } else {
                // 遇到 OR，将当前 AND 组作为一个操作数
                orOperands.add(createAndCondition(currentAndGroup));
                currentAndGroup.clear();
                currentAndGroup.add(node.condition);
            }
        }

        // 添加最后一个 AND 组
        orOperands.add(createAndCondition(currentAndGroup));

        // 如果只有一个操作数，直接返回
        if (orOperands.size() == 1) {
            return orOperands.get(0);
        }

        // 否则创建 OR 组合
        return new CompositeCondition(LogicalOperator.OR, orOperands, evaluationMode);
    }

    /**
     * 创建 AND 条件
     * 如果只有一个条件，直接返回；否则创建 CompositeCondition
     */
    private Condition createAndCondition(List<Condition> conditions) {
        if (conditions.size() == 1) {
            return conditions.get(0);
        }
        return new CompositeCondition(LogicalOperator.AND, conditions, evaluationMode);
    }

    public static void main(String[] args) {
        /**
         * 条件1 and (条件2 or 条件3)
         */

        String jsonStr = "{\"invoiceCode\":\"0001\",\"invoiceTime\":\"2025-10-21\",\"details\":[{\"code\":\"1\",\"name\":\"测试数据\",\"obj\":{\"info\":\"你好世界\",\"array\":[\"难顶\"]}},{\"code\":\"1\",\"name\":\"测试数据\"},\"测试数据\",\"测试数据01\"]}";
        FactContext factContext = new MapFactContext(jsonStr);
        System.out.println(factContext.getFactNames());
        // 创建配置解析元素对象
        Element element = new ConfigurableElement("账号编码", "invoiceCode", "to_bigdecimal", new MapPathResolver());

        Condition condition = ConditionBuilder.create().evaluationMode(EvaluationMode.COLLECT_ALL)
                .with(LeafCondition.builder().element(element)
                        .expectedValue(1)
                        .simple(true)
                        .failureCode("JY001")
                        .failureMessage("账号编码不符合要求")
                        .operator(OperatorRegistry.getOperator("="))
                        .build())
                .andGroup(ConditionBuilder.create()
                        .with(LeafCondition.builder().element(element)
                                .expectedValue(1)
                                .simple(true)
                                .failureCode("JY001")
                                .failureMessage("账号编码不符合要求")
                                .operator(OperatorRegistry.getOperator("<"))
                                .build())
                        .or(LeafCondition.builder().element(element)
                                .expectedValue(1)
                                .simple(true)
                                .failureCode("JY001")
                                .failureMessage("账号编码不符合要求")
                                .operator(OperatorRegistry.getOperator(">"))
                                .build()))
                .build();
        ConditionResult procResult = condition.evaluate(factContext);

        System.out.println(procResult);
    }
}
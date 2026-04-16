package com.ruleframe.core.condition;

import com.ruleframe.core.fact.FactContext;

/**
 * 叶子条件：包含元素、运算器、预期值
 */
public class LeafCondition implements Condition {

    @Override
    public boolean evaluate(FactContext ctx) {
        // TODO: 实现叶子条件的评估逻辑
        return false;
    }
}
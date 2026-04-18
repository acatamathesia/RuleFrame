package com.ruleframe.core.condition;

import com.ruleframe.core.condition.visitor.ConditionVisitor;
import com.ruleframe.core.fact.FactContext;

public interface Condition {
    ConditionResult evaluate(FactContext ctx);

    <T> T accept(ConditionVisitor<T> visitor);
}

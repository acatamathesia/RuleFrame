package com.ruleframe.core.condition.visitor;

import com.ruleframe.core.condition.CompositeCondition;
import com.ruleframe.core.condition.LeafCondition;

public interface ConditionVisitor<T> {
    T visit(LeafCondition condition);

    T visit(CompositeCondition condition);
}

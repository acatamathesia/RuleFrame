package com.ruleframe.core.condition;

import com.ruleframe.core.fact.FactContext;

public interface Condition {
    ConditionResult evaluate(FactContext ctx);
}

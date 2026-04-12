package com.ruleframe.core.condition;

import com.ruleframe.core.fact.FactContext;

public interface Condition {
    boolean evaluate(FactContext ctx);
}

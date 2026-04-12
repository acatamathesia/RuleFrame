package com.ruleframe.core.element;

import com.ruleframe.core.fact.FactContext;

public interface Element {
    String getName();
    ElementValue resolve(FactContext context);
}

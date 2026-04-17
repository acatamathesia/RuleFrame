package com.ruleframe.core.condition.builder;

import com.ruleframe.core.condition.Condition;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConditionBuilder {

    public static ConditionBuilder create() {
        return new ConditionBuilder();
    }

    public ConditionBuilder and(Condition condition) {
        return this;
    }

    public ConditionBuilder or(Condition condition) {
        return this;
    }

    public Condition build() {
        return null;
    }
}
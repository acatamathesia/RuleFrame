package com.ruleframe.core;

@FunctionalInterface
public interface Rule {
    void execute();
    default String getName() {
        return this.getClass().getSimpleName();
    }
}
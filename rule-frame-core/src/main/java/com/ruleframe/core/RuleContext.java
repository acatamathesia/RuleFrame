package com.ruleframe.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RuleContext {
    private String name;
    private List<Rule> rules = new ArrayList<>();

    public void addRule(Rule rule) {
        this.rules.add(rule);
    }

    public void removeRule(Rule rule) {
        this.rules.remove(rule);
    }
}
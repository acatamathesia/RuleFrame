package com.ruleframe.core;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RuleEngine {

    public void execute(Rule rule) {
        log.info("Executing rule: {}", rule.getName());
        rule.execute();
    }

    public void execute(RuleContext context) {
        log.info("Executing rules in context");
        context.getRules().forEach(this::execute);
    }
}
package com.ruleframe.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RuleEngineTest {

    @Test
    void testExecuteSingleRule() {
        RuleEngine engine = new RuleEngine();
        Rule rule = () -> System.out.println("Test rule executed");
        engine.execute(rule);
    }

    @Test
    void testExecuteRuleContext() {
        RuleEngine engine = new RuleEngine();
        RuleContext context = new RuleContext();
        context.setName("Test Context");
        context.addRule(() -> System.out.println("Rule 1 executed"));
        context.addRule(() -> System.out.println("Rule 2 executed"));
        engine.execute(context);
    }
}
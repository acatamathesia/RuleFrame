package com.ruleframe.web.controller;

import com.ruleframe.core.Rule;
import com.ruleframe.core.RuleContext;
import com.ruleframe.core.RuleEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleController {

    private final RuleEngine ruleEngine;

    @PostMapping("/execute")
    public Map<String, Object> executeRule(@RequestBody RuleRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        Rule rule = () -> {
            System.out.println("Executing rule: " + request.getRuleName());
        };
        
        ruleEngine.execute(rule);
        
        result.put("status", "success");
        result.put("message", "Rule executed successfully: " + request.getRuleName());
        return result;
    }

    @PostMapping("/context/execute")
    public Map<String, Object> executeContext(@RequestBody ContextRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        RuleContext context = new RuleContext();
        context.setName(request.getContextName());
        
        request.getRuleNames().forEach(ruleName -> {
            context.addRule(() -> System.out.println("Executing rule: " + ruleName));
        });
        
        ruleEngine.execute(context);
        
        result.put("status", "success");
        result.put("message", "Context executed successfully: " + request.getContextName());
        result.put("rulesCount", context.getRules().size());
        return result;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        return status;
    }
}
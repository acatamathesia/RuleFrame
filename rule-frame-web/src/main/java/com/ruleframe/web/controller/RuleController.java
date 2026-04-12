package com.ruleframe.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleController {


    @PostMapping("/execute")
    public Map<String, Object> executeRule(@RequestBody RuleRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        
        result.put("status", "success");
        result.put("message", "Rule executed successfully: " + request.getRuleName());
        return result;
    }

    @PostMapping("/context/execute")
    public Map<String, Object> executeContext(@RequestBody ContextRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        
        result.put("status", "success");
        result.put("message", "Context executed successfully: " + request.getContextName());
        return result;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        return status;
    }
}
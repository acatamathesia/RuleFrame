package com.ruleframe.web.controller;

import com.ruleframe.web.dto.ApiResponse;
import com.ruleframe.web.dto.RuleExecuteRequest;
import com.ruleframe.web.rule.service.RuleEngineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleController {

    private final RuleEngineService ruleEngineService;

    @PostMapping("/execute")
    public ApiResponse<Map<String, Object>> executeRule(@RequestBody RuleExecuteRequest request) {
        try {
            String mode = request.getMode() != null ? request.getMode() : "GROUP";
            Map<String, Object> result;
            if ("SINGLE".equalsIgnoreCase(mode)) {
                result = ruleEngineService.executeSingleRule(
                        request.getRuleCode(), request.getFacts());
            } else {
                result = ruleEngineService.executeByGroupCode(
                        request.getGroupCode(), request.getFacts());
            }
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("规则执行失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("allPassed", false);
            error.put("error", e.getMessage());
            return ApiResponse.success(error);
        }
    }

    @GetMapping("/health")
    public ApiResponse<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        return ApiResponse.success(status);
    }
}

package com.ruleframe.web.rule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruleframe.core.fact.FactContext;
import com.ruleframe.core.fact.MapFactContext;
import com.ruleframe.core.group.GroupEvaluator;
import com.ruleframe.core.group.GroupResult;
import com.ruleframe.core.group.RuleGroup;
import com.ruleframe.web.rule.parser.DefaultRuleConfigParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuleEngineService {

    private final DefaultRuleConfigParser configParser;
    private final GroupEvaluator groupEvaluator = new GroupEvaluator();
    private final ObjectMapper objectMapper;

    public Map<String, Object> executeByGroupCode(String groupCode, Map<String, Object> factsMap) {
        List<RuleGroup> groups = configParser.parseByGroupCode(groupCode);
        if (groups.isEmpty()) {
            throw new RuntimeException("未找到启用的规则组: " + groupCode);
        }

        FactContext factContext = buildFactContext(factsMap);
        List<GroupResult> groupResults = new ArrayList<>();
        for (RuleGroup group : groups) {
            GroupResult result = groupEvaluator.evaluate(group, factContext);
            groupResults.add(result);
        }

        return buildResponse(groupResults);
    }

    public Map<String, Object> executeSingleRule(String ruleCode, Map<String, Object> factsMap) {
        RuleGroup group = configParser.parseSingleRule(ruleCode);
        FactContext factContext = buildFactContext(factsMap);
        GroupResult result = groupEvaluator.evaluate(group, factContext);
        return buildResponse(List.of(result));
    }

    private FactContext buildFactContext(Map<String, Object> factsMap) {
        try {
            Map<String, Object> flatMap = flattenMap(factsMap, "");
            String json = objectMapper.writeValueAsString(flatMap);
            return new MapFactContext(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("构建FactContext失败: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> flattenMap(Map<String, Object> input, String prefix) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "/" + entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nestedMap = (Map<String, Object>) value;
                result.putAll(flattenMap(nestedMap, key));
            } else {
                result.put(key, value);
            }
        }
        return result;
    }

    private Map<String, Object> buildResponse(List<GroupResult> groupResults) {
        boolean allPassed = groupResults.stream().allMatch(GroupResult::isAllPassed);

        List<Map<String, Object>> groupDetails = new ArrayList<>();
        for (GroupResult gr : groupResults) {
            Map<String, Object> detail = new LinkedHashMap<>();
            detail.put("allPassed", gr.isAllPassed());

            List<Map<String, Object>> ruleDetails = new ArrayList<>();
            if (gr.getAllResults() != null) {
                for (var rr : gr.getAllResults()) {
                    Map<String, Object> rd = new LinkedHashMap<>();
                    rd.put("ruleId", rr.getRuleId());
                    rd.put("ruleName", rr.getRuleName());
                    rd.put("passed", rr.isPassed());
                    if (!rr.isPassed() && rr.getFailureResult() != null) {
                        rd.put("message", rr.getFailureResult());
                    }
                    ruleDetails.add(rd);
                }
            }
            detail.put("rules", ruleDetails);
            groupDetails.add(detail);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("allPassed", allPassed);
        response.put("groupCount", groupResults.size());
        response.put("groups", groupDetails);
        return response;
    }
}

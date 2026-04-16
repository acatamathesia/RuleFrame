package com.ruleframe.config.parser.dto;

import java.util.List;

/**
 * 规则DTO
 */
public class RuleDto {

    private String ruleId;
    private String ruleName;
    private int priority;
    private List<ConditionDto> conditions;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<ConditionDto> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionDto> conditions) {
        this.conditions = conditions;
    }
}
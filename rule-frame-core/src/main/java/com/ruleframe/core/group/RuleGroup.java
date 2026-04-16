package com.ruleframe.core.group;

import com.ruleframe.core.rule.Rule;
import java.util.List;

/**
 * 规则组：包含规则列表、执行策略枚举
 */
public class RuleGroup {

    private String groupId;
    private String groupName;
    private List<Rule> rules;
    private ExecutionStrategyEnum strategy;

    public enum ExecutionStrategyEnum {
        ALL_MATCH,      // 全部匹配
        FIRST_FAIL,     // 首次失败
        FIRST_SUCCESS   // 首次成功
    }

    public RuleGroup() {
    }

    public RuleGroup(String groupId, String groupName, List<Rule> rules, ExecutionStrategyEnum strategy) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.rules = rules;
        this.strategy = strategy;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public ExecutionStrategyEnum getStrategy() {
        return strategy;
    }

    public void setStrategy(ExecutionStrategyEnum strategy) {
        this.strategy = strategy;
    }
}
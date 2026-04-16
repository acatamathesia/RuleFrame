package com.ruleframe.core.group;

import com.ruleframe.core.rule.RuleResult;
import java.util.List;

/**
 * 组执行结果：是否全部通过、失败规则列表
 */
public class GroupResult {

    private boolean allPassed;
    private List<RuleResult> failedRules;
    private List<RuleResult> allResults;

    public GroupResult() {
    }

    public GroupResult(boolean allPassed, List<RuleResult> failedRules, List<RuleResult> allResults) {
        this.allPassed = allPassed;
        this.failedRules = failedRules;
        this.allResults = allResults;
    }

    public boolean isAllPassed() {
        return allPassed;
    }

    public void setAllPassed(boolean allPassed) {
        this.allPassed = allPassed;
    }

    public List<RuleResult> getFailedRules() {
        return failedRules;
    }

    public void setFailedRules(List<RuleResult> failedRules) {
        this.failedRules = failedRules;
    }

    public List<RuleResult> getAllResults() {
        return allResults;
    }

    public void setAllResults(List<RuleResult> allResults) {
        this.allResults = allResults;
    }
}
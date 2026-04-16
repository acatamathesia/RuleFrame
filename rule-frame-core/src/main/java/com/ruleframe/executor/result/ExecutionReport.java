package com.ruleframe.executor.result;

import com.ruleframe.core.group.GroupResult;
import com.ruleframe.core.rule.RuleResult;

import java.util.List;

/**
 * 最终执行报告，便于前端展示
 */
public class ExecutionReport {

    private String groupKey;
    private boolean success;
    private long duration;
    private GroupResult groupResult;
    private List<RuleResult> ruleResults;
    private String errorMessage;

    public ExecutionReport() {
    }

    public ExecutionReport(String groupKey, boolean success, long duration, GroupResult groupResult, List<RuleResult> ruleResults) {
        this.groupKey = groupKey;
        this.success = success;
        this.duration = duration;
        this.groupResult = groupResult;
        this.ruleResults = ruleResults;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public GroupResult getGroupResult() {
        return groupResult;
    }

    public void setGroupResult(GroupResult groupResult) {
        this.groupResult = groupResult;
    }

    public List<RuleResult> getRuleResults() {
        return ruleResults;
    }

    public void setRuleResults(List<RuleResult> ruleResults) {
        this.ruleResults = ruleResults;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 创建成功的执行报告
     * @param groupKey 规则组键
     * @param duration 执行耗时
     * @param groupResult 组结果
     * @param ruleResults 规则结果列表
     * @return 执行报告
     */
    public static ExecutionReport success(String groupKey, long duration, GroupResult groupResult, List<RuleResult> ruleResults) {
        return new ExecutionReport(groupKey, true, duration, groupResult, ruleResults);
    }

    /**
     * 创建失败的执行报告
     * @param groupKey 规则组键
     * @param duration 执行耗时
     * @param errorMessage 错误信息
     * @return 执行报告
     */
    public static ExecutionReport failure(String groupKey, long duration, String errorMessage) {
        ExecutionReport report = new ExecutionReport();
        report.setGroupKey(groupKey);
        report.setSuccess(false);
        report.setDuration(duration);
        report.setErrorMessage(errorMessage);
        return report;
    }
}
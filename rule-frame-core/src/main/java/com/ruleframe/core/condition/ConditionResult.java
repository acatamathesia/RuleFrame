package com.ruleframe.core.condition;

/**
 * 条件计算结果（含失败原因）
 */
public class ConditionResult {

    private boolean success;
    private String failureReason;

    public ConditionResult(boolean success) {
        this.success = success;
    }

    public ConditionResult(boolean success, String failureReason) {
        this.success = success;
        this.failureReason = failureReason;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public static ConditionResult success() {
        return new ConditionResult(true);
    }

    public static ConditionResult failure(String reason) {
        return new ConditionResult(false, reason);
    }
}
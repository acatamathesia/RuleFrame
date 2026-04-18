package com.ruleframe.core.condition;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.ToString;

/**
 * 条件计算结果（含失败原因）
 */
@ToString
public class ConditionResult {

    private boolean passed;
    private String failureReason;
    private Map<String, Object> metadata;

    {
        // 每次对象实例化的时候, 初始化元数据映射
        metadata = new HashMap<String, Object>(16);
    }

    public Object getMetadata(String key) {
        return metadata.get(key);
    }

    public ConditionResult setMetadata(String key, Object value) {
        metadata.put(key, value);
        return this;
    }

    public void setAllMetadata(Map<String, Object> metadata) {
        this.metadata.putAll(metadata);
    }

    public Set<String> listMetaDataKeys() {
        return metadata.keySet();
    }

    public ConditionResult(boolean passed) {
        this.passed = passed;
    }

    public ConditionResult(boolean passed, String failureReason) {
        this.passed = passed;
        this.failureReason = failureReason;
    }

    public boolean isPassed() {
        return passed;
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
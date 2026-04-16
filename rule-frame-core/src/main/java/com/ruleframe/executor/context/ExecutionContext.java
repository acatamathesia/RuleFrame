package com.ruleframe.executor.context;

import com.ruleframe.core.fact.FactContext;

/**
 * 执行上下文：包含 FactContext、执行追踪信息(耗时、路径)
 */
public class ExecutionContext {

    private FactContext factContext;
    private long startTime;
    private long endTime;
    private String executionPath;

    public ExecutionContext(FactContext factContext) {
        this.factContext = factContext;
        this.startTime = System.currentTimeMillis();
    }

    public FactContext getFactContext() {
        return factContext;
    }

    public void setFactContext(FactContext factContext) {
        this.factContext = factContext;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        if (endTime == 0) {
            return System.currentTimeMillis() - startTime;
        }
        return endTime - startTime;
    }

    public String getExecutionPath() {
        return executionPath;
    }

    public void setExecutionPath(String executionPath) {
        this.executionPath = executionPath;
    }

    /**
     * 标记执行完成
     */
    public void markCompleted() {
        this.endTime = System.currentTimeMillis();
    }
}
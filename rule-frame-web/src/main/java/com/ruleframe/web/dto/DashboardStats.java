package com.ruleframe.web.dto;

import lombok.Data;

/**
 * 仪表盘统计数据
 */
@Data
public class DashboardStats {

    /**
     * 规则总数
     */
    private Long totalRules;

    /**
     * 用户总数
     */
    private Long totalUsers;

    /**
     * 今日执行次数
     */
    private Long todayExecutions;

    /**
     * 成功率
     */
    private Double successRate;

    /**
     * 活跃规则数
     */
    private Long activeRules;

    /**
     * 系统运行天数
     */
    private Long runningDays;
}

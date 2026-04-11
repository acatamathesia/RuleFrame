package com.ruleframe.web.dto;

import lombok.Data;

/**
 * 系统信息
 */
@Data
public class SystemInfo {

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用版本
     */
    private String version;

    /**
     * 运行时间（毫秒）
     */
    private Long uptime;

    /**
     * Java版本
     */
    private String javaVersion;

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 系统架构
     */
    private String osArch;

    /**
     * CPU核心数
     */
    private Integer cpuCores;

    /**
     * 总内存（MB）
     */
    private Long totalMemory;

    /**
     * 可用内存（MB）
     */
    private Long freeMemory;

    /**
     * 已使用内存（MB）
     */
    private Long usedMemory;
}

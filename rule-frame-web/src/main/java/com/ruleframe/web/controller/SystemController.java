package com.ruleframe.web.controller;

import com.ruleframe.web.dto.ApiResponse;
import com.ruleframe.web.dto.DashboardStats;
import com.ruleframe.web.dto.SystemInfo;
import com.ruleframe.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * 系统监控控制器
 */
@RestController
@RequestMapping("/api/system")
@RequiredArgsConstructor
public class SystemController {

    private final UserService userService;

    @Value("${spring.application.name:RuleFrame}")
    private String appName;

    @Value("${application.version:1.0.0}")
    private String version;

    // 应用启动时间
    private static final LocalDateTime startTime = LocalDateTime.now();

    /**
     * 获取系统信息
     */
    @GetMapping("/info")
    public ApiResponse<SystemInfo> getSystemInfo() {
        SystemInfo info = new SystemInfo();
        info.setAppName(appName);
        info.setVersion(version);

        // 运行时信息
        Runtime runtime = Runtime.getRuntime();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

        // 运行时间（毫秒）
        info.setUptime(runtimeMXBean.getUptime());

        // Java信息
        info.setJavaVersion(System.getProperty("java.version"));

        // 操作系统信息
        info.setOsName(System.getProperty("os.name"));
        info.setOsArch(System.getProperty("os.arch"));
        info.setCpuCores(runtime.availableProcessors());

        // 内存信息（MB）
        long totalMemory = runtime.totalMemory() / (1024 * 1024);
        long freeMemory = runtime.freeMemory() / (1024 * 1024);
        info.setTotalMemory(totalMemory);
        info.setFreeMemory(freeMemory);
        info.setUsedMemory(totalMemory - freeMemory);

        return ApiResponse.success(info);
    }

    /**
     * 获取仪表盘统计数据
     */
    @GetMapping("/dashboard")
    public ApiResponse<DashboardStats> getDashboardStats() {
        DashboardStats stats = new DashboardStats();

        // 用户总数
        stats.setTotalUsers(userService.countUsers());

        // 模拟其他数据
        stats.setTotalRules(128L);
        stats.setTodayExecutions(52432L);
        stats.setSuccessRate(98.5);
        stats.setActiveRules(115L);

        // 计算运行天数
        long days = ChronoUnit.DAYS.between(startTime, LocalDateTime.now());
        stats.setRunningDays(days + 1);

        return ApiResponse.success(stats);
    }

    /**
     * 获取应用启动时间
     */
    @GetMapping("/start-time")
    public ApiResponse<LocalDateTime> getStartTime() {
        return ApiResponse.success(startTime);
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("UP");
    }
}

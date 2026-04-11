package com.ruleframe.web.controller;

import com.ruleframe.web.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 系统监控接口测试类
 * 测试系统信息、仪表盘统计、健康检查等接口
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String token;

    /**
     * 前置操作：登录获取Token
     */
    @BeforeEach
    void setUp() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        ApiResponse<LoginResponse> response = objectMapper.readValue(
                responseJson,
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, LoginResponse.class)
        );
        token = response.getData().getToken();
    }

    /**
     * 测试1: 获取系统信息
     */
    @Test
    @Order(1)
    @DisplayName("测试获取系统信息")
    void testGetSystemInfo() throws Exception {
        mockMvc.perform(get("/api/system/info")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.appName").isNotEmpty())
                .andExpect(jsonPath("$.data.version").isNotEmpty())
                .andExpect(jsonPath("$.data.javaVersion").isNotEmpty())
                .andExpect(jsonPath("$.data.osName").isNotEmpty())
                .andExpect(jsonPath("$.data.cpuCores").isNumber())
                .andExpect(jsonPath("$.data.totalMemory").isNumber())
                .andExpect(jsonPath("$.data.usedMemory").isNumber())
                .andExpect(jsonPath("$.data.freeMemory").isNumber());

        // 打印系统信息
        MvcResult result = mockMvc.perform(get("/api/system/info")
                .header("Authorization", "Bearer " + token))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        System.out.println("✅ 系统信息: " + responseJson);
    }

    /**
     * 测试2: 获取仪表盘统计数据
     */
    @Test
    @Order(2)
    @DisplayName("测试获取仪表盘统计数据")
    void testGetDashboardStats() throws Exception {
        mockMvc.perform(get("/api/system/dashboard")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalUsers").isNumber())
                .andExpect(jsonPath("$.data.totalRules").isNumber())
                .andExpect(jsonPath("$.data.todayExecutions").isNumber())
                .andExpect(jsonPath("$.data.successRate").isNumber())
                .andExpect(jsonPath("$.data.activeRules").isNumber())
                .andExpect(jsonPath("$.data.runningDays").isNumber());

        // 打印统计数据
        MvcResult result = mockMvc.perform(get("/api/system/dashboard")
                .header("Authorization", "Bearer " + token))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        System.out.println("✅ 仪表盘统计: " + responseJson);
    }

    /**
     * 测试3: 获取应用启动时间
     */
    @Test
    @Order(3)
    @DisplayName("测试获取应用启动时间")
    void testGetStartTime() throws Exception {
        mockMvc.perform(get("/api/system/start-time")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // 打印启动时间
        MvcResult result = mockMvc.perform(get("/api/system/start-time")
                .header("Authorization", "Bearer " + token))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        System.out.println("✅ 应用启动时间: " + responseJson);
    }

    /**
     * 测试4: 健康检查
     */
    @Test
    @Order(4)
    @DisplayName("测试健康检查")
    void testHealth() throws Exception {
        mockMvc.perform(get("/api/system/health")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("UP"));

        System.out.println("✅ 系统健康状态: UP");
    }

    /**
     * 测试5: 健康检查 - 无需Token（公开接口）
     */
    @Test
    @Order(5)
    @DisplayName("测试健康检查 - 无需Token")
    void testHealthWithoutToken() throws Exception {
        mockMvc.perform(get("/api/system/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("UP"));
    }

    /**
     * 测试6: 获取系统信息 - 验证内存信息合理性
     */
    @Test
    @Order(6)
    @DisplayName("测试获取系统信息 - 验证内存信息合理性")
    void testSystemInfoMemoryValidation() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/system/info")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> data = objectMapper.readValue(responseJson, java.util.Map.class);
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> systemInfo = (java.util.Map<String, Object>) data.get("data");

        long totalMemory = ((Number) systemInfo.get("totalMemory")).longValue();
        long usedMemory = ((Number) systemInfo.get("usedMemory")).longValue();
        long freeMemory = ((Number) systemInfo.get("freeMemory")).longValue();

        // 验证内存信息合理性
        assertTrue(totalMemory > 0, "总内存应该大于0");
        assertTrue(usedMemory > 0, "已用内存应该大于0");
        assertTrue(freeMemory > 0, "空闲内存应该大于0");
        assertEquals(totalMemory, usedMemory + freeMemory, "总内存应该等于已用内存加空闲内存");

        System.out.println("✅ 内存信息验证通过: 总=" + totalMemory + "MB, 已用=" + usedMemory + "MB, 空闲=" + freeMemory + "MB");
    }

    /**
     * 测试7: 获取系统信息 - 验证Java版本
     */
    @Test
    @Order(7)
    @DisplayName("测试获取系统信息 - 验证Java版本")
    void testSystemInfoJavaVersion() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/system/info")
                .header("Authorization", "Bearer " + token))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> data = objectMapper.readValue(responseJson, java.util.Map.class);
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> systemInfo = (java.util.Map<String, Object>) data.get("data");

        String javaVersion = (String) systemInfo.get("javaVersion");
        assertNotNull(javaVersion, "Java版本不应为空");
        assertTrue(javaVersion.startsWith("17") || javaVersion.startsWith("21"), 
                "Java版本应该是17或21，实际: " + javaVersion);

        System.out.println("✅ Java版本: " + javaVersion);
    }

    /**
     * 测试8: 获取仪表盘统计 - 验证用户数
     */
    @Test
    @Order(8)
    @DisplayName("测试获取仪表盘统计 - 验证用户数")
    void testDashboardUserCount() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/system/dashboard")
                .header("Authorization", "Bearer " + token))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> data = objectMapper.readValue(responseJson, java.util.Map.class);
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> stats = (java.util.Map<String, Object>) data.get("data");

        long totalUsers = ((Number) stats.get("totalUsers")).longValue();
        assertTrue(totalUsers > 0, "用户总数应该大于0（至少有admin用户）");

        System.out.println("✅ 用户总数: " + totalUsers);
    }

    /**
     * 测试9: 未携带Token访问受保护接口
     */
    @Test
    @Order(9)
    @DisplayName("测试未携带Token访问受保护接口")
    void testSystemInfoWithoutToken() throws Exception {
        mockMvc.perform(get("/api/system/info"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * 测试10: 未携带Token访问受保护接口 - 仪表盘
     */
    @Test
    @Order(10)
    @DisplayName("测试未携带Token访问受保护接口 - 仪表盘")
    void testDashboardWithoutToken() throws Exception {
        mockMvc.perform(get("/api/system/dashboard"))
                .andExpect(status().isUnauthorized());
    }
}

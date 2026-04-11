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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 规则引擎接口测试类
 * 测试规则执行、上下文执行等接口
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RuleControllerTest {

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
     * 测试1: 执行单个规则
     */
    @Test
    @Order(1)
    @DisplayName("测试执行单个规则")
    void testExecuteRule() throws Exception {
        RuleRequest request = new RuleRequest();
        request.setRuleName("TestRule");

        MvcResult result = mockMvc.perform(post("/api/rules/execute")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        System.out.println("✅ 规则执行结果: " + responseJson);
    }

    /**
     * 测试2: 执行规则上下文（多个规则）
     */
    @Test
    @Order(2)
    @DisplayName("测试执行规则上下文（多个规则）")
    void testExecuteContext() throws Exception {
        ContextRequest request = new ContextRequest();
        request.setContextName("TestContext");
        request.setRuleNames(Arrays.asList("Rule1", "Rule2", "Rule3"));

        MvcResult result = mockMvc.perform(post("/api/rules/context/execute")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.rulesCount").value(3))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        System.out.println("✅ 规则上下文执行结果: " + responseJson);
    }

    /**
     * 测试3: 规则引擎健康检查
     */
    @Test
    @Order(3)
    @DisplayName("测试规则引擎健康检查")
    void testHealth() throws Exception {
        mockMvc.perform(get("/api/rules/health")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));

        System.out.println("✅ 规则引擎健康状态: UP");
    }

    /**
     * 测试4: 执行规则 - 空规则名
     */
    @Test
    @Order(4)
    @DisplayName("测试执行规则 - 空规则名")
    void testExecuteRuleWithEmptyName() throws Exception {
        RuleRequest request = new RuleRequest();
        request.setRuleName("");

        mockMvc.perform(post("/api/rules/execute")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    /**
     * 测试5: 执行规则上下文 - 空规则列表
     */
    @Test
    @Order(5)
    @DisplayName("测试执行规则上下文 - 空规则列表")
    void testExecuteContextWithEmptyRules() throws Exception {
        ContextRequest request = new ContextRequest();
        request.setContextName("EmptyContext");
        request.setRuleNames(Arrays.asList());

        MvcResult result = mockMvc.perform(post("/api/rules/context/execute")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.rulesCount").value(0))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        System.out.println("✅ 空规则上下文执行结果: " + responseJson);
    }

    /**
     * 测试6: 执行规则 - 特殊字符规则名
     */
    @Test
    @Order(6)
    @DisplayName("测试执行规则 - 特殊字符规则名")
    void testExecuteRuleWithSpecialChars() throws Exception {
        RuleRequest request = new RuleRequest();
        request.setRuleName("Test_Rule-123@#$");

        mockMvc.perform(post("/api/rules/execute")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Rule executed successfully: Test_Rule-123@#$"));
    }

    /**
     * 测试7: 执行规则上下文 - 单个规则
     */
    @Test
    @Order(7)
    @DisplayName("测试执行规则上下文 - 单个规则")
    void testExecuteContextWithSingleRule() throws Exception {
        ContextRequest request = new ContextRequest();
        request.setContextName("SingleRuleContext");
        request.setRuleNames(Arrays.asList("SingleRule"));

        MvcResult result = mockMvc.perform(post("/api/rules/context/execute")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.rulesCount").value(1))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        System.out.println("✅ 单规则上下文执行结果: " + responseJson);
    }

    /**
     * 测试8: 执行规则上下文 - 大量规则
     */
    @Test
    @Order(8)
    @DisplayName("测试执行规则上下文 - 大量规则")
    void testExecuteContextWithManyRules() throws Exception {
        ContextRequest request = new ContextRequest();
        request.setContextName("ManyRulesContext");
        
        // 创建20个规则
        java.util.List<String> ruleNames = new java.util.ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            ruleNames.add("Rule" + i);
        }
        request.setRuleNames(ruleNames);

        MvcResult result = mockMvc.perform(post("/api/rules/context/execute")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.rulesCount").value(20))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        System.out.println("✅ 大量规则上下文执行结果: " + responseJson);
    }

    /**
     * 测试9: 执行规则 - 中文规则名
     */
    @Test
    @Order(9)
    @DisplayName("测试执行规则 - 中文规则名")
    void testExecuteRuleWithChineseName() throws Exception {
        RuleRequest request = new RuleRequest();
        request.setRuleName("测试规则");

        mockMvc.perform(post("/api/rules/execute")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Rule executed successfully: 测试规则"));
    }

    /**
     * 测试10: 未携带Token访问接口
     */
    @Test
    @Order(10)
    @DisplayName("测试未携带Token访问接口")
    void testExecuteRuleWithoutToken() throws Exception {
        RuleRequest request = new RuleRequest();
        request.setRuleName("TestRule");

        mockMvc.perform(post("/api/rules/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    /**
     * 测试11: 规则引擎健康检查 - 未携带Token
     */
    @Test
    @Order(11)
    @DisplayName("测试规则引擎健康检查 - 未携带Token")
    void testHealthWithoutToken() throws Exception {
        mockMvc.perform(get("/api/rules/health"))
                .andExpect(status().isUnauthorized());
    }
}

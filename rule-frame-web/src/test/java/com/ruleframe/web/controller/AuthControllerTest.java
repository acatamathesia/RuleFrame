package com.ruleframe.web.controller;

import com.ruleframe.web.dto.LoginRequest;
import com.ruleframe.web.dto.LoginResponse;
import com.ruleframe.web.dto.ApiResponse;
import com.ruleframe.web.dto.UserDTO;
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
 * 认证接口测试类
 * 测试登录、登出、获取用户信息、刷新Token等接口
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 存储登录后的Token，供其他测试使用
    private static String token;
    private static Long userId;
    private static String username;

    /**
     * 测试1: 用户登录 - 成功
     */
    @Test
    @Order(1)
    @DisplayName("测试用户登录 - 成功")
    void testLoginSuccess() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.user").isNotEmpty())
                .andExpect(jsonPath("$.data.user.username").value("admin"))
                .andExpect(jsonPath("$.data.menus").isArray())
                .andReturn();

        // 解析响应获取Token
        String responseJson = result.getResponse().getContentAsString();
        ApiResponse<LoginResponse> response = objectMapper.readValue(
                responseJson, 
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, LoginResponse.class)
        );
        
        token = response.getData().getToken();
        userId = response.getData().getUser().getId();
        username = response.getData().getUser().getUsername();
        
        assertNotNull(token, "Token不应为空");
        System.out.println("✅ 登录成功，Token: " + token.substring(0, 30) + "...");
    }

    /**
     * 测试2: 用户登录 - 密码错误
     */
    @Test
    @Order(2)
    @DisplayName("测试用户登录 - 密码错误")
    void testLoginWrongPassword() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("用户名或密码错误"));
    }

    /**
     * 测试3: 用户登录 - 用户不存在
     */
    @Test
    @Order(3)
    @DisplayName("测试用户登录 - 用户不存在")
    void testLoginUserNotFound() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistentuser");
        request.setPassword("password");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("用户名或密码错误"));
    }

    /**
     * 测试4: 获取当前用户信息
     */
    @Test
    @Order(4)
    @DisplayName("测试获取当前用户信息")
    void testGetProfile() throws Exception {
        assertNotNull(token, "请先运行testLoginSuccess获取Token");

        mockMvc.perform(get("/api/auth/profile")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(userId))
                .andExpect(jsonPath("$.data.username").value(username));
    }

    /**
     * 测试5: 获取当前用户信息 - 未携带Token
     */
    @Test
    @Order(5)
    @DisplayName("测试获取当前用户信息 - 未携带Token")
    void testGetProfileWithoutToken() throws Exception {
        mockMvc.perform(get("/api/auth/profile"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * 测试6: 刷新Token
     */
    @Test
    @Order(6)
    @DisplayName("测试刷新Token")
    void testRefreshToken() throws Exception {
        assertNotNull(token, "请先运行testLoginSuccess获取Token");

        MvcResult result = mockMvc.perform(post("/api/auth/refresh")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andReturn();

        // 解析新Token
        String responseJson = result.getResponse().getContentAsString();
        ApiResponse<String> response = objectMapper.readValue(
                responseJson, 
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, String.class)
        );
        
        String newToken = response.getData();
        assertNotNull(newToken, "新Token不应为空");
        assertNotEquals(token, newToken, "新Token应该与旧Token不同");
        System.out.println("✅ Token刷新成功，新Token: " + newToken.substring(0, 30) + "...");
    }

    /**
     * 测试7: 用户登出
     */
    @Test
    @Order(7)
    @DisplayName("测试用户登出")
    void testLogout() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试8: 刷新Token - Token无效
     */
    @Test
    @Order(8)
    @DisplayName("测试刷新Token - Token无效")
    void testRefreshTokenWithInvalidToken() throws Exception {
        mockMvc.perform(post("/api/auth/refresh")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }
}

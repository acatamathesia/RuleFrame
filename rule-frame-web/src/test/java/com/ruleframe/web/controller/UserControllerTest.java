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
 * 用户管理接口测试类
 * 测试用户的增删改查等操作
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String token;
    private static Long testUserId;

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
     * 测试1: 分页查询用户列表
     */
    @Test
    @Order(1)
    @DisplayName("测试分页查询用户列表")
    void testListUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + token)
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.list").isArray())
                .andExpect(jsonPath("$.data.total").isNumber())
                .andExpect(jsonPath("$.data.pageNum").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(10));
    }

    /**
     * 测试2: 分页查询用户列表 - 带关键词搜索
     */
    @Test
    @Order(2)
    @DisplayName("测试分页查询用户列表 - 带关键词搜索")
    void testListUsersWithKeyword() throws Exception {
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + token)
                .param("keyword", "admin")
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.list").isArray());
    }

    /**
     * 测试3: 创建用户
     */
    @Test
    @Order(3)
    @DisplayName("测试创建用户")
    void testCreateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser_" + System.currentTimeMillis());
        request.setPassword("test123");
        request.setNickname("测试用户");
        request.setEmail("test@example.com");
        request.setPhone("13800138000");
        request.setRole("user");

        MvcResult result = mockMvc.perform(post("/api/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value(request.getUsername()))
                .andExpect(jsonPath("$.data.nickname").value("测试用户"))
                .andReturn();

        // 保存创建的用户ID，用于后续测试
        String responseJson = result.getResponse().getContentAsString();
        ApiResponse<UserDTO> response = objectMapper.readValue(
                responseJson,
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, UserDTO.class)
        );
        testUserId = response.getData().getId();
        System.out.println("✅ 创建用户成功，用户ID: " + testUserId);
    }

    /**
     * 测试4: 根据ID获取用户
     */
    @Test
    @Order(4)
    @DisplayName("测试根据ID获取用户")
    void testGetUserById() throws Exception {
        assertNotNull(testUserId, "请先运行testCreateUser创建测试用户");

        mockMvc.perform(get("/api/users/{id}", testUserId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(testUserId));
    }

    /**
     * 测试5: 更新用户
     */
    @Test
    @Order(5)
    @DisplayName("测试更新用户")
    void testUpdateUser() throws Exception {
        assertNotNull(testUserId, "请先运行testCreateUser创建测试用户");

        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(testUserId);
        request.setNickname("更新后的昵称");
        request.setEmail("updated@example.com");

        mockMvc.perform(put("/api/users/{id}", testUserId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.nickname").value("更新后的昵称"))
                .andExpect(jsonPath("$.data.email").value("updated@example.com"));
    }

    /**
     * 测试6: 切换用户状态
     */
    @Test
    @Order(6)
    @DisplayName("测试切换用户状态")
    void testToggleUserStatus() throws Exception {
        assertNotNull(testUserId, "请先运行testCreateUser创建测试用户");

        mockMvc.perform(put("/api/users/{id}/toggle-status", testUserId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(testUserId));
    }

    /**
     * 测试7: 修改密码
     */
    @Test
    @Order(7)
    @DisplayName("测试修改密码")
    void testChangePassword() throws Exception {
        assertNotNull(testUserId, "请先运行testCreateUser创建测试用户");

        mockMvc.perform(put("/api/users/{id}/password", testUserId)
                .header("Authorization", "Bearer " + token)
                .param("oldPassword", "test123")
                .param("newPassword", "newpass123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试8: 更新个人信息
     */
    @Test
    @Order(8)
    @DisplayName("测试更新个人信息")
    void testUpdateProfile() throws Exception {
        assertNotNull(testUserId, "请先运行testCreateUser创建测试用户");

        mockMvc.perform(put("/api/users/{id}/profile", testUserId)
                .header("Authorization", "Bearer " + token)
                .param("nickname", "新昵称")
                .param("email", "newprofile@example.com")
                .param("phone", "13900139000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.nickname").value("新昵称"));
    }

    /**
     * 测试9: 删除用户
     */
    @Test
    @Order(9)
    @DisplayName("测试删除用户")
    void testDeleteUser() throws Exception {
        assertNotNull(testUserId, "请先运行testCreateUser创建测试用户");

        mockMvc.perform(delete("/api/users/{id}", testUserId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        System.out.println("✅ 删除用户成功，用户ID: " + testUserId);
        testUserId = null;
    }

    /**
     * 测试10: 获取不存在的用户
     */
    @Test
    @Order(10)
    @DisplayName("测试获取不存在的用户")
    void testGetNonExistentUser() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 999999)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("用户不存在"));
    }

    /**
     * 测试11: 创建重复用户名的用户
     */
    @Test
    @Order(11)
    @DisplayName("测试创建重复用户名的用户")
    void testCreateDuplicateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("admin"); // 已存在的用户名
        request.setPassword("test123");
        request.setNickname("重复用户");
        request.setEmail("duplicate@example.com");
        request.setPhone("13800138001");
        request.setRole("user");

        mockMvc.perform(post("/api/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andExpect(jsonPath("$.message").value("用户名已存在"));
    }

    /**
     * 测试12: 未携带Token访问接口
     */
    @Test
    @Order(12)
    @DisplayName("测试未携带Token访问接口")
    void testListUsersWithoutToken() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andExpect(status().isUnauthorized());
    }
}

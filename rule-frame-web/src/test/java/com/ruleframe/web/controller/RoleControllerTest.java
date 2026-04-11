package com.ruleframe.web.controller;

import com.ruleframe.web.dto.*;
import com.ruleframe.web.entity.Role;
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
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 角色管理接口测试类
 * 测试角色的增删改查、分配菜单等操作
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String token;
    private static Long testRoleId;

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
     * 测试1: 分页查询角色列表
     */
    @Test
    @Order(1)
    @DisplayName("测试分页查询角色列表")
    void testGetRolePage() throws Exception {
        mockMvc.perform(get("/api/roles/page")
                .header("Authorization", "Bearer " + token)
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray())
                .andExpect(jsonPath("$.data.total").isNumber())
                .andExpect(jsonPath("$.data.current").value(1))
                .andExpect(jsonPath("$.data.size").value(10));
    }

    /**
     * 测试2: 分页查询角色列表 - 带角色名称搜索
     */
    @Test
    @Order(2)
    @DisplayName("测试分页查询角色列表 - 带角色名称搜索")
    void testGetRolePageWithRoleName() throws Exception {
        mockMvc.perform(get("/api/roles/page")
                .header("Authorization", "Bearer " + token)
                .param("pageNum", "1")
                .param("pageSize", "10")
                .param("roleName", "admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray());
    }

    /**
     * 测试3: 查询所有角色
     */
    @Test
    @Order(3)
    @DisplayName("测试查询所有角色")
    void testGetAllRoles() throws Exception {
        mockMvc.perform(get("/api/roles/all")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 测试4: 创建角色
     */
    @Test
    @Order(4)
    @DisplayName("测试创建角色")
    void testCreateRole() throws Exception {
        Role role = new Role();
        role.setRoleName("测试角色");
        role.setRoleCode("TEST_ROLE_" + System.currentTimeMillis());
        role.setDescription("这是一个测试角色");
        role.setStatus(1);
        role.setSort(100);

        MvcResult result = mockMvc.perform(post("/api/roles")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.roleName").value("测试角色"))
                .andReturn();

        // 保存创建的角色ID
        String responseJson = result.getResponse().getContentAsString();
        ApiResponse<Role> response = objectMapper.readValue(
                responseJson,
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, Role.class)
        );
        testRoleId = response.getData().getId();
        System.out.println("✅ 创建角色成功，角色ID: " + testRoleId);
    }

    /**
     * 测试5: 根据ID查询角色
     */
    @Test
    @Order(5)
    @DisplayName("测试根据ID查询角色")
    void testGetRoleById() throws Exception {
        assertNotNull(testRoleId, "请先运行testCreateRole创建测试角色");

        mockMvc.perform(get("/api/roles/{id}", testRoleId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(testRoleId))
                .andExpect(jsonPath("$.data.roleName").value("测试角色"));
    }

    /**
     * 测试6: 更新角色
     */
    @Test
    @Order(6)
    @DisplayName("测试更新角色")
    void testUpdateRole() throws Exception {
        assertNotNull(testRoleId, "请先运行testCreateRole创建测试角色");

        Role role = new Role();
        role.setId(testRoleId);
        role.setRoleName("更新后的角色");
        role.setRoleCode("UPDATED_TEST_ROLE");
        role.setDescription("更新后的描述");
        role.setStatus(1);
        role.setSort(99);

        mockMvc.perform(put("/api/roles/{id}", testRoleId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.roleName").value("更新后的角色"))
                .andExpect(jsonPath("$.data.description").value("更新后的描述"));
    }

    /**
     * 测试7: 更新角色状态
     */
    @Test
    @Order(7)
    @DisplayName("测试更新角色状态")
    void testUpdateRoleStatus() throws Exception {
        assertNotNull(testRoleId, "请先运行testCreateRole创建测试角色");

        Map<String, Integer> request = new HashMap<>();
        request.put("status", 0);

        mockMvc.perform(put("/api/roles/{id}/status", testRoleId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试8: 为角色分配菜单
     */
    @Test
    @Order(8)
    @DisplayName("测试为角色分配菜单")
    void testAssignMenus() throws Exception {
        assertNotNull(testRoleId, "请先运行testCreateRole创建测试角色");

        // 先获取所有菜单
        MvcResult menuResult = mockMvc.perform(get("/api/menus/all")
                .header("Authorization", "Bearer " + token))
                .andReturn();

        String menuResponseJson = menuResult.getResponse().getContentAsString();
        ApiResponse<List> menuResponse = objectMapper.readValue(
                menuResponseJson,
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, List.class)
        );

        List<?> menus = menuResponse.getData();
        // 取前几个菜单ID进行分配
        List<Long> menuIds = menus.stream()
                .limit(3)
                .map(menu -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> menuMap = (Map<String, Object>) menu;
                    return ((Number) menuMap.get("id")).longValue();
                })
                .toList();

        Map<String, List<Long>> request = new HashMap<>();
        request.put("menuIds", menuIds);

        mockMvc.perform(post("/api/roles/{roleId}/menus", testRoleId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        System.out.println("✅ 为角色分配菜单成功，菜单数量: " + menuIds.size());
    }

    /**
     * 测试9: 查询角色的菜单ID列表
     */
    @Test
    @Order(9)
    @DisplayName("测试查询角色的菜单ID列表")
    void testGetRoleMenuIds() throws Exception {
        assertNotNull(testRoleId, "请先运行testCreateRole创建测试角色");

        mockMvc.perform(get("/api/roles/{roleId}/menus", testRoleId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 测试10: 批量删除角色
     */
    @Test
    @Order(10)
    @DisplayName("测试批量删除角色")
    void testDeleteRoles() throws Exception {
        // 先创建一个测试角色
        Role role = new Role();
        role.setRoleName("批量删除角色");
        role.setRoleCode("BATCH_DELETE_ROLE_" + System.currentTimeMillis());
        role.setDescription("用于批量删除测试的角色");
        role.setStatus(1);

        MvcResult result = mockMvc.perform(post("/api/roles")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        ApiResponse<Role> response = objectMapper.readValue(
                responseJson,
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, Role.class)
        );
        Long roleId = response.getData().getId();

        // 批量删除
        mockMvc.perform(delete("/api/roles/batch")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Arrays.asList(roleId))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        System.out.println("✅ 批量删除角色成功");
    }

    /**
     * 测试11: 删除角色
     */
    @Test
    @Order(11)
    @DisplayName("测试删除角色")
    void testDeleteRole() throws Exception {
        assertNotNull(testRoleId, "请先运行testCreateRole创建测试角色");

        mockMvc.perform(delete("/api/roles/{id}", testRoleId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        System.out.println("✅ 删除角色成功，角色ID: " + testRoleId);
        testRoleId = null;
    }

    /**
     * 测试12: 查询不存在的角色
     */
    @Test
    @Order(12)
    @DisplayName("测试查询不存在的角色")
    void testGetNonExistentRole() throws Exception {
        mockMvc.perform(get("/api/roles/{id}", 999999)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * 测试13: 未携带Token访问接口
     */
    @Test
    @Order(13)
    @DisplayName("测试未携带Token访问接口")
    void testGetRolesWithoutToken() throws Exception {
        mockMvc.perform(get("/api/roles/all"))
                .andExpect(status().isUnauthorized());
    }
}

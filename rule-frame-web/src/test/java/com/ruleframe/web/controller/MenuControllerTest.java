package com.ruleframe.web.controller;

import com.ruleframe.web.dto.*;
import com.ruleframe.web.entity.Menu;
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
 * 菜单管理接口测试类
 * 测试菜单的增删改查等操作
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String token;
    private static Long testMenuId;

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
     * 测试1: 查询菜单树
     */
    @Test
    @Order(1)
    @DisplayName("测试查询菜单树")
    void testGetMenuTree() throws Exception {
        mockMvc.perform(get("/api/menus/tree")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 测试2: 查询所有菜单
     */
    @Test
    @Order(2)
    @DisplayName("测试查询所有菜单")
    void testGetAllMenus() throws Exception {
        mockMvc.perform(get("/api/menus/all")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 测试3: 创建菜单
     */
    @Test
    @Order(3)
    @DisplayName("测试创建菜单")
    void testCreateMenu() throws Exception {
        Menu menu = new Menu();
        menu.setMenuName("测试菜单");
        menu.setMenuCode("TEST_MENU_" + System.currentTimeMillis());
        menu.setMenuType(1);
        menu.setParentId(0L);
        menu.setPath("/test");
        menu.setComponent("Test");
        menu.setIcon("test");
        menu.setSort(100);
        menu.setVisible(1);
        menu.setStatus(1);

        MvcResult result = mockMvc.perform(post("/api/menus")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menu)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.menuName").value("测试菜单"))
                .andReturn();

        // 保存创建的菜单ID
        String responseJson = result.getResponse().getContentAsString();
        ApiResponse<Menu> response = objectMapper.readValue(
                responseJson,
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, Menu.class)
        );
        testMenuId = response.getData().getId();
        System.out.println("✅ 创建菜单成功，菜单ID: " + testMenuId);
    }

    /**
     * 测试4: 根据ID查询菜单
     */
    @Test
    @Order(4)
    @DisplayName("测试根据ID查询菜单")
    void testGetMenuById() throws Exception {
        assertNotNull(testMenuId, "请先运行testCreateMenu创建测试菜单");

        mockMvc.perform(get("/api/menus/{id}", testMenuId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(testMenuId))
                .andExpect(jsonPath("$.data.menuName").value("测试菜单"));
    }

    /**
     * 测试5: 更新菜单
     */
    @Test
    @Order(5)
    @DisplayName("测试更新菜单")
    void testUpdateMenu() throws Exception {
        assertNotNull(testMenuId, "请先运行testCreateMenu创建测试菜单");

        Menu menu = new Menu();
        menu.setId(testMenuId);
        menu.setMenuName("更新后的菜单");
        menu.setMenuCode("UPDATED_TEST_MENU");
        menu.setMenuType(1);
        menu.setParentId(0L);
        menu.setPath("/updated-test");
        menu.setSort(99);

        mockMvc.perform(put("/api/menus/{id}", testMenuId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menu)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.menuName").value("更新后的菜单"))
                .andExpect(jsonPath("$.data.path").value("/updated-test"));
    }

    /**
     * 测试6: 更新菜单状态
     */
    @Test
    @Order(6)
    @DisplayName("测试更新菜单状态")
    void testUpdateMenuStatus() throws Exception {
        assertNotNull(testMenuId, "请先运行testCreateMenu创建测试菜单");

        Map<String, Integer> request = new HashMap<>();
        request.put("status", 0);

        mockMvc.perform(put("/api/menus/{id}/status", testMenuId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试7: 根据用户ID查询菜单树
     */
    @Test
    @Order(7)
    @DisplayName("测试根据用户ID查询菜单树")
    void testGetMenuTreeByUserId() throws Exception {
        mockMvc.perform(get("/api/menus/user/1")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 测试8: 批量删除菜单
     */
    @Test
    @Order(8)
    @DisplayName("测试批量删除菜单")
    void testDeleteMenus() throws Exception {
        // 先创建几个测试菜单
        Menu menu1 = new Menu();
        menu1.setMenuName("批量删除菜单1");
        menu1.setMenuCode("BATCH_DELETE_1_" + System.currentTimeMillis());
        menu1.setMenuType(1);
        menu1.setParentId(0L);
        menu1.setSort(100);
        menu1.setStatus(1);

        MvcResult result1 = mockMvc.perform(post("/api/menus")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menu1)))
                .andReturn();

        String responseJson1 = result1.getResponse().getContentAsString();
        ApiResponse<Menu> response1 = objectMapper.readValue(
                responseJson1,
                objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, Menu.class)
        );
        Long menuId1 = response1.getData().getId();

        // 批量删除
        mockMvc.perform(delete("/api/menus/batch")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Arrays.asList(menuId1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        System.out.println("✅ 批量删除菜单成功");
    }

    /**
     * 测试9: 删除菜单
     */
    @Test
    @Order(9)
    @DisplayName("测试删除菜单")
    void testDeleteMenu() throws Exception {
        assertNotNull(testMenuId, "请先运行testCreateMenu创建测试菜单");

        mockMvc.perform(delete("/api/menus/{id}", testMenuId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        System.out.println("✅ 删除菜单成功，菜单ID: " + testMenuId);
        testMenuId = null;
    }

    /**
     * 测试10: 查询不存在的菜单
     */
    @Test
    @Order(10)
    @DisplayName("测试查询不存在的菜单")
    void testGetNonExistentMenu() throws Exception {
        mockMvc.perform(get("/api/menus/{id}", 999999)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * 测试11: 未携带Token访问接口
     */
    @Test
    @Order(11)
    @DisplayName("测试未携带Token访问接口")
    void testGetMenuTreeWithoutToken() throws Exception {
        mockMvc.perform(get("/api/menus/tree"))
                .andExpect(status().isUnauthorized());
    }
}

package com.ruleframe.web.controller;

import com.ruleframe.web.dto.ApiResponse;
import com.ruleframe.web.entity.Menu;
import com.ruleframe.web.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 菜单控制器
 */
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * 查询菜单树
     */
    @GetMapping("/tree")
    public ApiResponse<List<Menu>> getMenuTree() {
        List<Menu> menuTree = menuService.getMenuTree();
        return ApiResponse.success(menuTree);
    }

    /**
     * 查询所有菜单
     */
    @GetMapping("/all")
    public ApiResponse<List<Menu>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();
        return ApiResponse.success(menus);
    }

    /**
     * 根据ID查询菜单
     */
    @GetMapping("/{id}")
    public ApiResponse<Menu> getMenuById(@PathVariable Long id) {
        Menu menu = menuService.getMenuById(id);
        return ApiResponse.success(menu);
    }

    /**
     * 创建菜单
     */
    @PostMapping
    public ApiResponse<Menu> createMenu(@RequestBody Menu menu) {
        Menu created = menuService.createMenu(menu);
        return ApiResponse.success(created);
    }

    /**
     * 更新菜单
     */
    @PutMapping("/{id}")
    public ApiResponse<Menu> updateMenu(@PathVariable Long id, @RequestBody Menu menu) {
        menu.setId(id);
        Menu updated = menuService.updateMenu(menu);
        return ApiResponse.success(updated);
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ApiResponse.success(null);
    }

    /**
     * 批量删除菜单
     */
    @DeleteMapping("/batch")
    public ApiResponse<Void> deleteMenus(@RequestBody List<Long> ids) {
        menuService.deleteMenus(ids);
        return ApiResponse.success(null);
    }

    /**
     * 更新菜单状态
     */
    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateMenuStatus(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        Integer status = request.get("status");
        menuService.updateMenuStatus(id, status);
        return ApiResponse.success(null);
    }

    /**
     * 根据用户ID查询菜单树
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<Menu>> getMenuTreeByUserId(@PathVariable Long userId) {
        List<Menu> menuTree = menuService.getMenuTreeByUserId(userId);
        return ApiResponse.success(menuTree);
    }
}

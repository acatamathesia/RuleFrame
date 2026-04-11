package com.ruleframe.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.dto.ApiResponse;
import com.ruleframe.web.entity.Role;
import com.ruleframe.web.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 分页查询角色
     */
    @GetMapping("/page")
    public ApiResponse<Page<Role>> getRolePage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String roleName) {
        Page<Role> page = roleService.getRolePage(pageNum, pageSize, roleName);
        return ApiResponse.success(page);
    }

    /**
     * 查询所有角色
     */
    @GetMapping("/all")
    public ApiResponse<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ApiResponse.success(roles);
    }

    /**
     * 根据ID查询角色
     */
    @GetMapping("/{id}")
    public ApiResponse<Role> getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return ApiResponse.success(role);
    }

    /**
     * 创建角色
     */
    @PostMapping
    public ApiResponse<Role> createRole(@RequestBody Role role) {
        Role created = roleService.createRole(role);
        return ApiResponse.success(created);
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    public ApiResponse<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        role.setId(id);
        Role updated = roleService.updateRole(role);
        return ApiResponse.success(updated);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiResponse.success(null);
    }

    /**
     * 批量删除角色
     */
    @DeleteMapping("/batch")
    public ApiResponse<Void> deleteRoles(@RequestBody List<Long> ids) {
        roleService.deleteRoles(ids);
        return ApiResponse.success(null);
    }

    /**
     * 更新角色状态
     */
    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateRoleStatus(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        Integer status = request.get("status");
        roleService.updateRoleStatus(id, status);
        return ApiResponse.success(null);
    }

    /**
     * 为角色分配菜单
     */
    @PostMapping("/{roleId}/menus")
    public ApiResponse<Void> assignMenus(@PathVariable Long roleId, @RequestBody Map<String, List<Long>> request) {
        List<Long> menuIds = request.get("menuIds");
        roleService.assignMenus(roleId, menuIds);
        return ApiResponse.success(null);
    }

    /**
     * 查询角色的菜单ID列表
     */
    @GetMapping("/{roleId}/menus")
    public ApiResponse<List<Long>> getRoleMenuIds(@PathVariable Long roleId) {
        List<Long> menuIds = roleService.getRoleMenuIds(roleId);
        return ApiResponse.success(menuIds);
    }
}

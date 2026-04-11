package com.ruleframe.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService {

    /**
     * 分页查询角色
     */
    Page<Role> getRolePage(int pageNum, int pageSize, String roleName);

    /**
     * 查询所有角色
     */
    List<Role> getAllRoles();

    /**
     * 根据ID查询角色
     */
    Role getRoleById(Long id);

    /**
     * 创建角色
     */
    Role createRole(Role role);

    /**
     * 更新角色
     */
    Role updateRole(Role role);

    /**
     * 删除角色
     */
    void deleteRole(Long id);

    /**
     * 批量删除角色
     */
    void deleteRoles(List<Long> ids);

    /**
     * 更新角色状态
     */
    void updateRoleStatus(Long id, Integer status);

    /**
     * 为角色分配菜单
     */
    void assignMenus(Long roleId, List<Long> menuIds);

    /**
     * 查询角色的菜单ID列表
     */
    List<Long> getRoleMenuIds(Long roleId);
}

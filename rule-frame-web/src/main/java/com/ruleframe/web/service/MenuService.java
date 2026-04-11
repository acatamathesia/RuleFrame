package com.ruleframe.web.service;

import com.ruleframe.web.entity.Menu;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface MenuService {

    /**
     * 查询所有菜单（树形结构）
     */
    List<Menu> getMenuTree();

    /**
     * 查询所有菜单（平铺列表）
     */
    List<Menu> getAllMenus();

    /**
     * 根据ID查询菜单
     */
    Menu getMenuById(Long id);

    /**
     * 创建菜单
     */
    Menu createMenu(Menu menu);

    /**
     * 更新菜单
     */
    Menu updateMenu(Menu menu);

    /**
     * 删除菜单
     */
    void deleteMenu(Long id);

    /**
     * 批量删除菜单
     */
    void deleteMenus(List<Long> ids);

    /**
     * 更新菜单状态
     */
    void updateMenuStatus(Long id, Integer status);

    /**
     * 根据用户ID查询菜单树
     */
    List<Menu> getMenuTreeByUserId(Long userId);
}

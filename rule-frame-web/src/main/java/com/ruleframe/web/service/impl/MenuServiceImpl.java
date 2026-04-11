package com.ruleframe.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruleframe.web.entity.Menu;
import com.ruleframe.web.mapper.MenuMapper;
import com.ruleframe.web.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;

    @Override
    public List<Menu> getMenuTree() {
        log.info("查询所有菜单（树形结构）");
        // 查询所有菜单
        List<Menu> allMenus = getAllMenus();
        log.info("查询到菜单总数: {}", allMenus.size());
        
        // 构建树形结构
        List<Menu> menuTree = buildMenuTree(allMenus, 0L);
        log.info("构建菜单树完成，根菜单数: {}", menuTree.size());
        return menuTree;
    }

    @Override
    public List<Menu> getAllMenus() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getStatus, 1)  // 只查询启用的菜单
                    .eq(Menu::getVisible, 1)  // 只查询可见的菜单
                    .orderByAsc(Menu::getSort);
        List<Menu> menus = menuMapper.selectList(queryWrapper);
        log.debug("查询所有菜单，条件: status=1, visible=1, 结果数量: {}", menus.size());
        return menus;
    }

    @Override
    public Menu getMenuById(Long id) {
        return menuMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Menu createMenu(Menu menu) {
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        menuMapper.insert(menu);
        return menu;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Menu updateMenu(Menu menu) {
        menu.setUpdateTime(LocalDateTime.now());
        menuMapper.updateById(menu);
        return menu;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        menuMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenus(List<Long> ids) {
        menuMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenuStatus(Long id, Integer status) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setStatus(status);
        menu.setUpdateTime(LocalDateTime.now());
        menuMapper.updateById(menu);
    }

    @Override
    public List<Menu> getMenuTreeByUserId(Long userId) {
        log.info("根据用户ID查询菜单树, userId={}", userId);
        // 根据用户ID查询菜单
        List<Menu> menus = menuMapper.selectMenusByUserId(userId);
        log.info("查询到用户菜单总数: {}", menus.size());
        
        // 构建树形结构
        List<Menu> menuTree = buildMenuTree(menus, 0L);
        log.info("构建用户菜单树完成，根菜单数: {}", menuTree.size());
        return menuTree;
    }

    /**
     * 构建菜单树
     */
    private List<Menu> buildMenuTree(List<Menu> menus, Long parentId) {
        return menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .peek(menu -> {
                    List<Menu> children = buildMenuTree(menus, menu.getId());
                    menu.setChildren(children);
                })
                .collect(Collectors.toList());
    }
}

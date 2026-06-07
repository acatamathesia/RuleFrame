-- ========================================
-- 菜单结构更新脚本
-- 将菜单调整为树形结构
-- 执行日期：2026-04-11
-- ========================================

USE ruleframe;

-- 1. 清空现有的菜单和关联数据
DELETE FROM sys_role_menu;
DELETE FROM sys_menu;

-- 2. 重新插入树形菜单结构
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_code`, `menu_type`, `path`, `component`, `icon`, `sort`, `permission`, `status`, `visible`) VALUES
-- 一级菜单
(0, '仪表盘', 'dashboard', 2, '/dashboard', 'Dashboard', 'DataAnalysis', 0, NULL, 1, 1),
(0, '规则管理', 'rules', 2, '/rules', 'Rules', 'Document', 1, NULL, 1, 1),
(0, '系统管理', 'system', 1, '/system', 'Layout', 'Setting', 2, NULL, 1, 1);

-- 获取系统管理菜单的ID（应该是3）
SET @system_menu_id = (SELECT id FROM sys_menu WHERE menu_code = 'system' LIMIT 1);

-- 插入系统管理的子菜单
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_code`, `menu_type`, `path`, `component`, `icon`, `sort`, `permission`, `status`, `visible`) VALUES
(@system_menu_id, '用户管理', 'users', 2, '/system/users', 'system/Users', 'User', 1, 'system:user:list', 1, 1),
(@system_menu_id, '角色管理', 'roles', 2, '/system/roles', 'system/Roles', 'UserFilled', 2, 'system:role:list', 1, 1),
(@system_menu_id, '菜单管理', 'menus', 2, '/system/menus', 'system/Menus', 'Menu', 3, 'system:menu:list', 1, 1),
(@system_menu_id, '系统设置', 'settings', 2, '/system/settings', 'system/Settings', 'Setting', 4, 'system:settings:view', 1, 1);

-- 3. 为超级管理员角色分配所有菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) 
SELECT 1, id FROM `sys_menu` WHERE deleted = 0;

-- 4. 查看更新后的菜单结构
SELECT 
    id,
    parent_id,
    menu_name,
    menu_code,
    menu_type,
    path,
    icon,
    sort
FROM sys_menu
WHERE deleted = 0
ORDER BY sort;

-- ========================================
-- 规则引擎子菜单
-- ========================================

-- 获取规则管理菜单的ID
SET @rules_menu_id = (SELECT id FROM sys_menu WHERE menu_code = 'rules' LIMIT 1);

INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_code`, `menu_type`, `path`, `component`, `icon`, `sort`, `permission`, `status`, `visible`) VALUES
(@rules_menu_id, '规则组管理', 'ruleGroup', 2, '/ruleEngine/ruleGroup', 'ruleEngine/RuleGroup', 'Collection', 1, '', 1, 1),
(@rules_menu_id, '规则定义管理', 'ruleDef', 2, '/ruleEngine/ruleDef', 'ruleEngine/RuleDef', 'List', 2, '', 1, 1),
(@rules_menu_id, '规则执行测试', 'ruleExecution', 2, '/ruleEngine/ruleExecution', 'ruleEngine/RuleExecution', 'VideoPlay', 3, '', 1, 1);

-- 为超级管理员角色分配所有新菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) 
SELECT 1, id FROM `sys_menu` WHERE deleted = 0;

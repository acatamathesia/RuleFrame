-- 在系统管理下添加字典管理菜单
-- 注意：系统管理的ID在init.sql中为3，请根据实际情况调整
-- 如果系统管理ID不是3，请先查询：SELECT id FROM sys_menu WHERE menu_code = 'system';

-- 添加字典管理菜单（排序5，在系统设置后面）
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_code`, `menu_type`, `path`, `component`, `icon`, `sort`, `permission`) VALUES
(3, '字典管理', 'dictionary', 2, '/system/dictionary', 'system/Dictionary', 'Notebook', 5, 'system:dict:list');

-- 为超级管理员角色分配新菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, id FROM `sys_menu` WHERE `menu_code` = 'dictionary';

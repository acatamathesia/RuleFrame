-- ========================================
-- 为规则元素管理添加菜单
-- 在规则管理下新增"规则元素管理"菜单
-- ========================================

USE ruleframe;

-- 获取规则管理菜单的ID
SET @rules_menu_id = (SELECT id FROM sys_menu WHERE menu_code = 'rules' LIMIT 1);

-- 插入规则元素管理菜单
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_code`, `menu_type`, `path`, `component`, `icon`, `sort`, `permission`, `status`, `visible`)
VALUES (@rules_menu_id, '规则元素管理', 'ruleElement', 2, '/ruleEngine/RuleElement', 'ruleEngine/RuleElement', 'Coin', 1, 'rule:element:list', 1, 1);

-- 为超级管理员角色分配新菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, id FROM `sys_menu` WHERE menu_code = 'ruleElement' AND deleted = 0;

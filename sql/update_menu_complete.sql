-- ========================================
-- RuleFrame 完整菜单配置脚本
-- 确保所有前后端菜单存在并授权给超级管理员
-- 执行日期: 2026-06-07
-- 特性: 幂等(可重复执行), 非破坏性
-- ========================================

USE ruleframe;

-- =========================================
-- 第一步: 修正已有菜单(如果从 init.sql 初始化)
-- =========================================

-- 修正 rules 菜单: 改为目录类型，使用 RouteView 布局
UPDATE sys_menu 
SET menu_type = 1, path = "/ruleEngine", component = "Layout"
WHERE menu_code = "rules" AND (menu_type != 1 OR path != "/ruleEngine");

-- =========================================
-- 第二步: 确保一级菜单存在(幂等)
-- =========================================

INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, permission, status, visible)
SELECT 0, "仪表盘", "dashboard", 2, "/dashboard", "Dashboard", "DataAnalysis", 0, NULL, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_code = "dashboard" AND deleted = 0);

INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, permission, status, visible)
SELECT 0, "规则引擎", "rules", 1, "/ruleEngine", "Layout", "Setting", 1, NULL, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_code = "rules" AND deleted = 0);

INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, permission, status, visible)
SELECT 0, "系统管理", "system", 1, "/system", "Layout", "Setting", 2, NULL, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_code = "system" AND deleted = 0);

-- =========================================
-- 第三步: 获取父菜单ID
-- =========================================

SET @rules_menu_id = (SELECT id FROM sys_menu WHERE menu_code = "rules" AND deleted = 0 LIMIT 1);
SET @system_menu_id = (SELECT id FROM sys_menu WHERE menu_code = "system" AND deleted = 0 LIMIT 1);

-- 修正历史数据: 如果子菜单的 parent_id 是硬编码的 3(init.sql)，修正为实际的 system id
UPDATE sys_menu 
SET parent_id = @system_menu_id
WHERE parent_id = 3 AND id != @system_menu_id AND parent_id != @system_menu_id;

-- =========================================
-- 第四步: 规则引擎子菜单
-- =========================================

-- 规则组管理
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, permission, status, visible)
SELECT @rules_menu_id, "规则组管理", "ruleGroup", 2, "/ruleEngine/RuleGroup", "ruleEngine/RuleGroup", "Collection", 1, "rule:group:list", 1, 1
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_code = "ruleGroup" AND deleted = 0);

-- 规则定义管理
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, permission, status, visible)
SELECT @rules_menu_id, "规则定义管理", "ruleDef", 2, "/ruleEngine/RuleDef", "ruleEngine/RuleDef", "Memo", 2, "rule:def:list", 1, 1
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_code = "ruleDef" AND deleted = 0);

-- 规则元素管理
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, permission, status, visible)
SELECT @rules_menu_id, "规则元素管理", "ruleElement", 2, "/ruleEngine/RuleElement", "ruleEngine/RuleElement", "Coin", 3, "rule:element:list", 1, 1
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_code = "ruleElement" AND deleted = 0);

-- 规则执行测试
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, permission, status, visible)
SELECT @rules_menu_id, "规则执行测试", "ruleExecution", 2, "/ruleEngine/RuleExecution", "ruleEngine/RuleExecution", "VideoPlay", 4, "rule:execution:test", 1, 1
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_code = "ruleExecution" AND deleted = 0);

-- =========================================
-- 第五步: 系统管理子菜单
-- =========================================

-- 用户管理
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, permission, status, visible)
SELECT @system_menu_id, "用户管理", "users", 2, "/system/users", "system/Users", "User", 1, "system:user:list", 1, 1
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_code = "users" AND deleted = 0);

-- 角色管理
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, permission, status, visible)
SELECT @system_menu_id, "角色管理", "roles", 2, "/system/roles", "system/Roles", "UserFilled", 2, "system:role:list", 1, 1
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_code = "roles" AND deleted = 0);

-- 菜单管理
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, permission, status, visible)
SELECT @system_menu_id, "菜单管理", "menus", 2, "/system/menus", "system/Menus", "Menu", 3, "system:menu:list", 1, 1
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_code = "menus" AND deleted = 0);

-- 字典管理
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, permission, status, visible)
SELECT @system_menu_id, "字典管理", "dictionary", 2, "/system/dictionary", "system/Dictionary", "Notebook", 4, "system:dict:list", 1, 1
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_code = "dictionary" AND deleted = 0);

-- 系统设置
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, permission, status, visible)
SELECT @system_menu_id, "系统设置", "settings", 2, "/system/settings", "system/Settings", "Tools", 5, "system:settings:view", 1, 1
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_code = "settings" AND deleted = 0);

-- =========================================
-- 第六步: 权限关联 - 为超级管理员(role_id=1)分配所有菜单
-- =========================================

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, m.id
FROM sys_menu m
WHERE m.deleted = 0
  AND NOT EXISTS (
    SELECT 1 FROM sys_role_menu rm
    WHERE rm.role_id = 1 AND rm.menu_id = m.id
  );

-- =========================================
-- 第七步: 验证结果
-- =========================================

SELECT "=== 菜单配置完成 ===" AS result;

SELECT 
    id,
    parent_id,
    menu_name,
    menu_code,
    CASE menu_type
        WHEN 1 THEN "目录"
        WHEN 2 THEN "菜单"
        WHEN 3 THEN "按钮"
    END AS menu_type_name,
    path,
    icon,
    sort,
    permission,
    CASE status WHEN 1 THEN "启用" ELSE "禁用" END AS status_name
FROM sys_menu
WHERE deleted = 0
ORDER BY sort, parent_id, id;

SELECT 
    COUNT(DISTINCT m.id) AS total_menus,
    COUNT(DISTINCT rm.menu_id) AS assigned_to_admin
FROM sys_menu m
LEFT JOIN sys_role_menu rm ON rm.menu_id = m.id AND rm.role_id = 1
WHERE m.deleted = 0;

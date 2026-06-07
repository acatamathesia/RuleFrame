-- ========================================
-- RuleFrame - 规则组与规则定义表
-- 创建日期：2025-07-10
-- ========================================

USE ruleframe;

-- 规则组定义表
DROP TABLE IF EXISTS `rule_group_def`;
CREATE TABLE `rule_group_def` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT "主键ID",
    `group_code` VARCHAR(64) NOT NULL COMMENT "规则组编码（唯一）",
    `group_name` VARCHAR(128) NOT NULL COMMENT "规则组名称",
    `strategy` VARCHAR(32) NOT NULL DEFAULT "ALL_MATCH" COMMENT "执行策略：ALL_MATCH/FIRST_FAIL/FIRST_SUCCESS",
    `description` VARCHAR(512) DEFAULT NULL COMMENT "描述",
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT "状态：0-禁用，1-启用",
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT "逻辑删除：0-未删除，1-已删除",
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "更新时间",
    UNIQUE KEY `uk_group_code` (`group_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT="规则组定义表";

-- 规则定义表
DROP TABLE IF EXISTS `rule_def`;
CREATE TABLE `rule_def` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT "主键ID",
    `group_id` BIGINT NOT NULL COMMENT "所属规则组ID",
    `rule_code` VARCHAR(64) NOT NULL COMMENT "规则编码",
    `rule_name` VARCHAR(128) NOT NULL COMMENT "规则名称",
    `priority` INT NOT NULL DEFAULT 0 COMMENT "优先级（数值越小优先级越高）",
    `conditions_json` TEXT DEFAULT NULL COMMENT "条件JSON（支持嵌套复合条件）",
    `result_action` VARCHAR(64) DEFAULT NULL COMMENT "结果动作编码",
    `result_message` VARCHAR(256) DEFAULT NULL COMMENT "结果消息模板",
    `unified_return` TINYINT NOT NULL DEFAULT 0 COMMENT "是否统一返回：0-否，1-是",
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT "状态：0-禁用，1-启用",
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT "逻辑删除：0-未删除，1-已删除",
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "更新时间",
    KEY `idx_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT="规则定义表";

-- ========================================
-- 测试数据
-- ========================================

-- 规则组：用户准入校验
INSERT INTO `rule_group_def` (`group_code`, `group_name`, `strategy`, `description`, `status`) VALUES
("USER_ADMISSION", "用户准入校验", "ALL_MATCH", "对新注册用户进行多维度的准入资格审查", 1);

-- 规则1：黑名单检查（优先级最高）
INSERT INTO `rule_def` (`group_id`, `rule_code`, `rule_name`, `priority`, `conditions_json`, `result_action`, `result_message`, `status`) VALUES
(LAST_INSERT_ID(), "BLACKLIST_CHECK", "黑名单检查", 0,
 "{\"logicalOperator\":\"AND\",\"conditions\":[{\"element\":\"inBlacklist\",\"operator\":\"EQUAL\",\"expectedValue\":false,\"failureCode\":\"BLACKLISTED\",\"failureMessage\":\"用户在黑名单中\"}]}",
 "REJECT", "用户已被列入黑名单", 1);

-- 规则2：年龄检查
INSERT INTO `rule_def` (`group_id`, `rule_code`, `rule_name`, `priority`, `conditions_json`, `result_action`, `result_message`, `status`) VALUES
(1, "AGE_CHECK", "年龄检查", 1,
 "{\"logicalOperator\":\"AND\",\"conditions\":[{\"element\":\"age\",\"operator\":\"GREATER_THAN_OR_EQUAL\",\"expectedValue\":18,\"failureCode\":\"AGE_TOO_YOUNG\",\"failureMessage\":\"用户年龄必须不小于18岁\"}]}",
 "REJECT", "年龄不符合要求", 1);

-- 规则3：信用评分检查
INSERT INTO `rule_def` (`group_id`, `rule_code`, `rule_name`, `priority`, `conditions_json`, `result_action`, `result_message`, `status`) VALUES
(1, "CREDIT_CHECK", "信用评分检查", 2,
 "{\"logicalOperator\":\"AND\",\"conditions\":[{\"element\":\"creditScore\",\"operator\":\"GREATER_THAN\",\"expectedValue\":600,\"failureCode\":\"LOW_CREDIT\",\"failureMessage\":\"信用评分必须大于600\"}]}",
 "REJECT", "信用评分不足", 1);

-- 规则4：VIP或高消费用户快速通道
INSERT INTO `rule_def` (`group_id`, `rule_code`, `rule_name`, `priority`, `conditions_json`, `result_action`, `result_message`, `status`) VALUES
(1, "VIP_OR_PREMIUM", "VIP或高消费用户检查", 3,
 "{\"logicalOperator\":\"OR\",\"conditions\":[{\"element\":\"vipLevel\",\"operator\":\"GREATER_THAN\",\"expectedValue\":0,\"failureCode\":\"NOT_VIP\",\"failureMessage\":\"非VIP用户\"},{\"element\":\"totalSpending\",\"operator\":\"GREATER_THAN\",\"expectedValue\":50000,\"failureCode\":\"LOW_SPENDING\",\"failureMessage\":\"累计消费不足50000\"}]}",
 "APPROVE", "VIP或高消费用户通过", 1);

-- ========================================
-- 菜单更新：添加规则组管理和规则测试菜单
-- ========================================

-- 规则组管理
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_code`, `menu_type`, `path`, `component`, `icon`, `sort`, `permission`, `status`, `visible`)
SELECT (SELECT id FROM sys_menu WHERE menu_code = "rules" LIMIT 1), "规则组管理", "ruleGroup", 2, "/ruleEngine/RuleGroup", "ruleEngine/RuleGroup", "Collection", 1, "rule:group:list", 1, 1
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_code = "ruleGroup");

-- 规则测试
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_code`, `menu_type`, `path`, `component`, `icon`, `sort`, `permission`, `status`, `visible`)
SELECT (SELECT id FROM sys_menu WHERE menu_code = "rules" LIMIT 1), "规则测试", "ruleTester", 2, "/ruleEngine/RuleTester", "ruleEngine/RuleTester", "VideoPlay", 2, "rule:tester:execute", 1, 1
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_code = "ruleTester");

-- 为超级管理员角色分配新菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, id FROM `sys_menu` WHERE menu_code IN ("ruleGroup", "ruleTester") AND deleted = 0
AND NOT EXISTS (SELECT 1 FROM `sys_role_menu` rm WHERE rm.role_id = 1 AND rm.menu_id = id);

SELECT "SQL执行完毕：表结构、测试数据、菜单已创建" AS result;

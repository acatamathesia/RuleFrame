-- ========================================
-- 规则元素种子数据
-- 用于支持测试规则示例
-- ========================================

USE ruleframe;

-- 插入规则元素（如果不存在）
INSERT IGNORE INTO `rule_set_element` (`code`, `name`, `el_path`, `converted`, `convert_type`, `enabled`, `deleted`, `create_user`, `create_time`, `update_time`)
VALUES
('inBlacklist', '是否在黑名单', '$.inBlacklist', 1, 'boolean', 1, 0, 'admin', NOW(), NOW()),
('age', '用户年龄', '$.age', 1, 'number', 1, 0, 'admin', NOW(), NOW()),
('creditScore', '信用评分', '$.creditScore', 1, 'number', 1, 0, 'admin', NOW(), NOW()),
('vipLevel', 'VIP等级', '$.vipLevel', 1, 'string', 1, 0, 'admin', NOW(), NOW()),
('totalSpending', '累计消费金额', '$.totalSpending', 1, 'number', 1, 0, 'admin', NOW(), NOW());

-- ========================================
-- 规则元素表（rule_set_element）
-- 用于存储规则配置中的元素信息
-- ========================================

USE ruleframe;

CREATE TABLE IF NOT EXISTS `rule_set_element` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `code` VARCHAR(50) NOT NULL COMMENT '元素编码',
    `name` VARCHAR(100) NOT NULL COMMENT '元素名称',
    `el_path` VARCHAR(500) NOT NULL COMMENT '元素路径（JSONPath表达式）',
    `converted` TINYINT DEFAULT 0 COMMENT '是否类型转换：0-否，1-是',
    `convert_type` VARCHAR(20) DEFAULT 'string' COMMENT '数据类型：string-字符串，number-数字，boolean-布尔，date-日期',
    `enabled` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    `create_user` VARCHAR(50) COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user` VARCHAR(50) COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `idx_convert_type` (`convert_type`),
    KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='规则元素表';

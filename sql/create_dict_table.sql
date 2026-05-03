-- 创建数据库（如果不存在）
USE ruleframe;

-- 字典类型表（父字典）
CREATE TABLE IF NOT EXISTS `sys_dict_type` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典类型ID',
    `dict_name` VARCHAR(100) NOT NULL COMMENT '字典名称',
    `dict_code` VARCHAR(50) NOT NULL COMMENT '字典编码',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `remark` VARCHAR(500) COMMENT '备注',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT COMMENT '创建人',
    `update_by` BIGINT COMMENT '更新人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dict_code` (`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典类型表（父字典）';

-- 字典数据表（子字典）
CREATE TABLE IF NOT EXISTS `sys_dict_data` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典数据ID',
    `dict_type_id` BIGINT NOT NULL COMMENT '所属字典类型ID',
    `dict_label` VARCHAR(100) NOT NULL COMMENT '字典标签',
    `dict_value` VARCHAR(100) NOT NULL COMMENT '字典键值',
    `dict_sort` INT DEFAULT 0 COMMENT '字典排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `remark` VARCHAR(500) COMMENT '备注',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT COMMENT '创建人',
    `update_by` BIGINT COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `idx_dict_type_id` (`dict_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典数据表（子字典）';

-- 插入示例字典类型数据
INSERT INTO `sys_dict_type` (`dict_name`, `dict_code`, `status`, `sort`, `remark`) VALUES
('用户性别', 'sys_user_sex', 1, 1, '用户性别字典'),
('系统开关', 'sys_yes_no', 1, 2, '系统开关字典'),
('通知状态', 'sys_notice_status', 1, 3, '通知状态字典');

-- 插入示例字典数据（用户性别）
INSERT INTO `sys_dict_data` (`dict_type_id`, `dict_label`, `dict_value`, `dict_sort`, `status`, `remark`) VALUES
(1, '男', '0', 1, 1, '性别男'),
(1, '女', '1', 2, 1, '性别女'),
(1, '未知', '2', 3, 1, '性别未知');

-- 插入示例字典数据（系统开关）
INSERT INTO `sys_dict_data` (`dict_type_id`, `dict_label`, `dict_value`, `dict_sort`, `status`, `remark`) VALUES
(2, '开启', 'true', 1, 1, '系统开关-开启'),
(2, '关闭', 'false', 2, 1, '系统开关-关闭');

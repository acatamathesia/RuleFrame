package com.ruleframe.web.rule.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 规则元素持久化对象
 */
@Data
@TableName("rule_set_element")
public class SetElement {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 元素编码
     */
    private String code;

    /**
     * 元素名称
     */
    private String name;

    /**
     * 元素路径（JSONPath表达式）
     */
    private String elPath;

    /**
     * 是否类型转换：0-否，1-是
     */
    private Integer converted;

    /**
     * 数据类型：string-字符串，number-数字，boolean-布尔，date-日期
     */
    private String convertType;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer enabled;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    private String updateUser;
}

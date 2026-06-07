package com.ruleframe.web.rule.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("rule_group_def")
public class RuleGroupDef {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String groupCode;

    private String groupName;

    private String strategy;

    private String description;

    private Integer status;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

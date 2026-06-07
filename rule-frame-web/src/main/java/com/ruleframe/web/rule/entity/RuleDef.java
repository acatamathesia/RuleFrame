package com.ruleframe.web.rule.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("rule_def")
public class RuleDef {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long groupId;

    private String ruleCode;

    private String ruleName;

    private Integer priority;

    private String conditionsJson;

    private String resultAction;

    private String resultMessage;

    private Integer unifiedReturn;

    private Integer status;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

package com.ruleframe.web.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RuleGroupDTO {
    private Long id;
    private String groupCode;
    private String groupName;
    private String strategy;
    private String description;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

package com.ruleframe.web.dto;

import lombok.Data;

@Data
public class RuleGroupUpdateRequest {
    private Long id;
    private String groupCode;
    private String groupName;
    private String strategy;
    private String description;
    private Integer status;
}

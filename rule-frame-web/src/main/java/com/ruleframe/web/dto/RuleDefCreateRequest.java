package com.ruleframe.web.dto;

import lombok.Data;

@Data
public class RuleDefCreateRequest {
    private Long groupId;
    private String ruleCode;
    private String ruleName;
    private Integer priority;
    private String conditionsJson;
    private String resultAction;
    private String resultMessage;
    private Integer unifiedReturn;
    private Integer status;
}

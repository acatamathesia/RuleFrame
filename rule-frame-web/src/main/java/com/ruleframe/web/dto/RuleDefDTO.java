package com.ruleframe.web.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RuleDefDTO {
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
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

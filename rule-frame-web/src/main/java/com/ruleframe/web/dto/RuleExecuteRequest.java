package com.ruleframe.web.dto;

import lombok.Data;
import java.util.Map;

@Data
public class RuleExecuteRequest {
    /** 规则组编码 */
    private String groupCode;
    /** 事实数据JSON对象 */
    private Map<String, Object> facts;
    /** 规则编码（可选，单规则执行时使用） */
    private String ruleCode;
    /** 执行模式：GROUP-规则组执行，SINGLE-单规则执行 */
    private String mode;
}

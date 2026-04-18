package com.ruleframe.core.rule;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RuleResult {

    private String ruleId;
    private String ruleName;
    private boolean isPassed;
    private String failureResult;

}

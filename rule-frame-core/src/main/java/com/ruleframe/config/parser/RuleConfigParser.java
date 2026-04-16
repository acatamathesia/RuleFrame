package com.ruleframe.config.parser;

import com.ruleframe.config.parser.dto.RuleGroupDto;
import com.ruleframe.core.group.RuleGroup;

/**
 * 将配置DTO解析为领域对象
 */
public interface RuleConfigParser {

    /**
     * 将规则组DTO解析为规则组领域对象
     * @param dto 规则组DTO
     * @return 规则组领域对象
     */
    RuleGroup parse(RuleGroupDto dto);
}
package com.ruleframe.web.rule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.dto.RuleDefCreateRequest;
import com.ruleframe.web.dto.RuleDefDTO;
import com.ruleframe.web.dto.RuleDefUpdateRequest;

import java.util.List;

public interface IRuleDefService {
    Page<RuleDefDTO> pageQuery(int pageNum, int pageSize, Long groupId, String keyword);
    List<RuleDefDTO> listByGroupId(Long groupId);
    List<RuleDefDTO> listByGroupCode(String groupCode);
    RuleDefDTO getById(Long id);
    RuleDefDTO getByRuleCode(String ruleCode);
    RuleDefDTO create(RuleDefCreateRequest request);
    RuleDefDTO update(RuleDefUpdateRequest request);
    void delete(Long id);
    void deleteBatch(List<Long> ids);
}

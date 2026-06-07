package com.ruleframe.web.rule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.dto.RuleGroupCreateRequest;
import com.ruleframe.web.dto.RuleGroupDTO;
import com.ruleframe.web.dto.RuleGroupUpdateRequest;

import java.util.List;

public interface IRuleGroupDefService {
    Page<RuleGroupDTO> pageQuery(int pageNum, int pageSize, String keyword);
    List<RuleGroupDTO> listAll();
    RuleGroupDTO getById(Long id);
    RuleGroupDTO getByGroupCode(String groupCode);
    RuleGroupDTO create(RuleGroupCreateRequest request);
    RuleGroupDTO update(RuleGroupUpdateRequest request);
    void delete(Long id);
    void deleteBatch(List<Long> ids);
}

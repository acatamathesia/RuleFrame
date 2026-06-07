package com.ruleframe.web.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.dto.RuleDefCreateRequest;
import com.ruleframe.web.dto.RuleDefDTO;
import com.ruleframe.web.dto.RuleDefUpdateRequest;
import com.ruleframe.web.rule.entity.RuleDef;
import com.ruleframe.web.rule.entity.RuleGroupDef;
import com.ruleframe.web.rule.mapper.RuleDefMapper;
import com.ruleframe.web.rule.mapper.RuleGroupDefMapper;
import com.ruleframe.web.rule.service.IRuleDefService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuleDefServiceImpl implements IRuleDefService {

    private final RuleDefMapper ruleDefMapper;
    private final RuleGroupDefMapper ruleGroupDefMapper;

    @Override
    public Page<RuleDefDTO> pageQuery(int pageNum, int pageSize, Long groupId, String keyword) {
        LambdaQueryWrapper<RuleDef> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RuleDef::getDeleted, 0);
        if (groupId != null) {
            wrapper.eq(RuleDef::getGroupId, groupId);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                .like(RuleDef::getRuleName, keyword)
                .or()
                .like(RuleDef::getRuleCode, keyword));
        }
        wrapper.orderByAsc(RuleDef::getGroupId)
            .orderByAsc(RuleDef::getPriority);

        Page<RuleDef> page = new Page<>(pageNum, pageSize);
        Page<RuleDef> entityPage = ruleDefMapper.selectPage(page, wrapper);

        Page<RuleDefDTO> dtoPage = new Page<>(pageNum, pageSize, entityPage.getTotal());
        dtoPage.setRecords(entityPage.getRecords().stream()
            .map(this::toDTO)
            .collect(Collectors.toList()));
        return dtoPage;
    }

    @Override
    public List<RuleDefDTO> listByGroupId(Long groupId) {
        LambdaQueryWrapper<RuleDef> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RuleDef::getGroupId, groupId)
            .eq(RuleDef::getDeleted, 0)
            .eq(RuleDef::getStatus, 1)
            .orderByAsc(RuleDef::getPriority);
        return ruleDefMapper.selectList(wrapper).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<RuleDefDTO> listByGroupCode(String groupCode) {
        RuleGroupDef group = ruleGroupDefMapper.selectOne(
            Wrappers.<RuleGroupDef>lambdaQuery()
                .eq(RuleGroupDef::getGroupCode, groupCode)
                .eq(RuleGroupDef::getDeleted, 0));
        if (group == null) {
            throw new RuntimeException("规则组不存在: " + groupCode);
        }
        return listByGroupId(group.getId());
    }

    @Override
    public RuleDefDTO getById(Long id) {
        RuleDef entity = ruleDefMapper.selectById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new RuntimeException("规则不存在");
        }
        return toDTO(entity);
    }

    @Override
    public RuleDefDTO getByRuleCode(String ruleCode) {
        LambdaQueryWrapper<RuleDef> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RuleDef::getRuleCode, ruleCode)
            .eq(RuleDef::getDeleted, 0);
        RuleDef entity = ruleDefMapper.selectOne(wrapper);
        if (entity == null) {
            throw new RuntimeException("规则不存在: " + ruleCode);
        }
        return toDTO(entity);
    }

    @Override
    @Transactional
    public RuleDefDTO create(RuleDefCreateRequest request) {
        RuleDef entity = new RuleDef();
        BeanUtils.copyProperties(request, entity);
        entity.setDeleted(0);
        ruleDefMapper.insert(entity);
        return toDTO(entity);
    }

    @Override
    @Transactional
    public RuleDefDTO update(RuleDefUpdateRequest request) {
        RuleDef entity = ruleDefMapper.selectById(request.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new RuntimeException("规则不存在");
        }
        BeanUtils.copyProperties(request, entity);
        ruleDefMapper.updateById(entity);
        return toDTO(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        RuleDef entity = ruleDefMapper.selectById(id);
        if (entity == null) {
            throw new RuntimeException("规则不存在");
        }
        entity.setDeleted(1);
        ruleDefMapper.updateById(entity);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    private RuleDefDTO toDTO(RuleDef entity) {
        RuleDefDTO dto = new RuleDefDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}

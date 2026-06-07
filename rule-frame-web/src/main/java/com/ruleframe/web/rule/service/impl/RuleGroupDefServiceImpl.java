package com.ruleframe.web.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.dto.RuleGroupCreateRequest;
import com.ruleframe.web.dto.RuleGroupDTO;
import com.ruleframe.web.dto.RuleGroupUpdateRequest;
import com.ruleframe.web.rule.entity.RuleGroupDef;
import com.ruleframe.web.rule.mapper.RuleGroupDefMapper;
import com.ruleframe.web.rule.service.IRuleGroupDefService;
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
public class RuleGroupDefServiceImpl implements IRuleGroupDefService {

    private final RuleGroupDefMapper ruleGroupDefMapper;

    @Override
    public Page<RuleGroupDTO> pageQuery(int pageNum, int pageSize, String keyword) {
        LambdaQueryWrapper<RuleGroupDef> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RuleGroupDef::getDeleted, 0);
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                .like(RuleGroupDef::getGroupName, keyword)
                .or()
                .like(RuleGroupDef::getGroupCode, keyword));
        }
        wrapper.orderByDesc(RuleGroupDef::getUpdateTime);

        Page<RuleGroupDef> page = new Page<>(pageNum, pageSize);
        Page<RuleGroupDef> entityPage = ruleGroupDefMapper.selectPage(page, wrapper);

        Page<RuleGroupDTO> dtoPage = new Page<>(pageNum, pageSize, entityPage.getTotal());
        dtoPage.setRecords(entityPage.getRecords().stream()
            .map(this::toDTO)
            .collect(Collectors.toList()));
        return dtoPage;
    }

    @Override
    public List<RuleGroupDTO> listAll() {
        LambdaQueryWrapper<RuleGroupDef> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RuleGroupDef::getDeleted, 0)
            .eq(RuleGroupDef::getStatus, 1)
            .orderByDesc(RuleGroupDef::getUpdateTime);
        return ruleGroupDefMapper.selectList(wrapper).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public RuleGroupDTO getById(Long id) {
        RuleGroupDef entity = ruleGroupDefMapper.selectById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new RuntimeException("规则组不存在");
        }
        return toDTO(entity);
    }

    @Override
    public RuleGroupDTO getByGroupCode(String groupCode) {
        LambdaQueryWrapper<RuleGroupDef> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RuleGroupDef::getGroupCode, groupCode)
            .eq(RuleGroupDef::getDeleted, 0);
        RuleGroupDef entity = ruleGroupDefMapper.selectOne(wrapper);
        if (entity == null) {
            throw new RuntimeException("规则组不存在: " + groupCode);
        }
        return toDTO(entity);
    }

    @Override
    @Transactional
    public RuleGroupDTO create(RuleGroupCreateRequest request) {
        RuleGroupDef entity = new RuleGroupDef();
        BeanUtils.copyProperties(request, entity);
        entity.setDeleted(0);
        ruleGroupDefMapper.insert(entity);
        return toDTO(entity);
    }

    @Override
    @Transactional
    public RuleGroupDTO update(RuleGroupUpdateRequest request) {
        RuleGroupDef entity = ruleGroupDefMapper.selectById(request.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new RuntimeException("规则组不存在");
        }
        BeanUtils.copyProperties(request, entity);
        ruleGroupDefMapper.updateById(entity);
        return toDTO(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        RuleGroupDef entity = ruleGroupDefMapper.selectById(id);
        if (entity == null) {
            throw new RuntimeException("规则组不存在");
        }
        entity.setDeleted(1);
        ruleGroupDefMapper.updateById(entity);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    private RuleGroupDTO toDTO(RuleGroupDef entity) {
        RuleGroupDTO dto = new RuleGroupDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}

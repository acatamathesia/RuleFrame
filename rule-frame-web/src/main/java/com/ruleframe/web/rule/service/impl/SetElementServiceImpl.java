package com.ruleframe.web.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.dto.SetElementCreateRequest;
import com.ruleframe.web.dto.SetElementDTO;
import com.ruleframe.web.dto.SetElementQueryRequest;
import com.ruleframe.web.dto.SetElementUpdateRequest;
import com.ruleframe.web.rule.entity.SetElement;
import com.ruleframe.web.rule.mapper.SetElementMapper;
import com.ruleframe.web.rule.service.ISetElementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 规则元素服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SetElementServiceImpl implements ISetElementService {

    private final SetElementMapper setElementMapper;

    @Override
    public Page<SetElementDTO> pageQuery(SetElementQueryRequest request) {
        log.info("分页查询规则元素，参数：{}", request);

        // 构建查询条件
        LambdaQueryWrapper<SetElement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetElement::getDeleted, 0);

        // 关键词模糊搜索（名称或路径）
        if (StringUtils.hasText(request.getKeyword())) {
            wrapper.and(w -> w
                    .like(SetElement::getName, request.getKeyword())
                    .or()
                    .like(SetElement::getElPath, request.getKeyword()));
        }

        // 数据类型筛选
        if (StringUtils.hasText(request.getDataType())) {
            wrapper.eq(SetElement::getConvertType, request.getDataType());
        }

        // 按更新时间降序
        wrapper.orderByDesc(SetElement::getUpdateTime);

        // 执行分页查询
        Page<SetElement> page = setElementMapper.selectPage(
                new Page<>(request.getPageNum(), request.getPageSize()),
                wrapper);

        // 转换为DTO
        Page<SetElementDTO> dtoPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        dtoPage.setRecords(convertToDTOList(page.getRecords()));
        return dtoPage;
    }

    @Override
    public List<SetElementDTO> listAll() {
        LambdaQueryWrapper<SetElement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetElement::getDeleted, 0);
        wrapper.eq(SetElement::getEnabled, 1);
        return convertToDTOList(setElementMapper.selectList(wrapper));
    }

    @Override
    public SetElementDTO getById(Long id) {
        SetElement entity = setElementMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        return convertToDTO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SetElementDTO create(SetElementCreateRequest request) {
        log.info("创建规则元素：{}", request);

        SetElement entity = new SetElement();
        BeanUtils.copyProperties(request, entity);

        // 默认启用
        if (entity.getEnabled() == null) {
            entity.setEnabled(1);
        }

        setElementMapper.insert(entity);
        return convertToDTO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SetElementDTO update(SetElementUpdateRequest request) {
        log.info("更新规则元素：{}", request);

        SetElement entity = setElementMapper.selectById(request.getId());
        if (entity == null) {
            throw new RuntimeException("规则元素不存在，id: " + request.getId());
        }

        BeanUtils.copyProperties(request, entity);
        setElementMapper.updateById(entity);
        return convertToDTO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.info("删除规则元素：id={}", id);
        setElementMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> ids) {
        log.info("批量删除规则元素：ids={}", ids);
        setElementMapper.deleteBatchIds(ids);
    }

    /**
     * 实体转DTO（适配前端字段名）
     */
    private SetElementDTO convertToDTO(SetElement entity) {
        if (entity == null) {
            return null;
        }
        SetElementDTO dto = new SetElementDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setElementName(entity.getName());
        dto.setElementPath(entity.getElPath());
        dto.setNeedConvert(entity.getConverted() != null && entity.getConverted() == 1);
        dto.setDataType(entity.getConvertType());
        return dto;
    }

    /**
     * 批量转换
     */
    private List<SetElementDTO> convertToDTOList(List<SetElement> entities) {
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}

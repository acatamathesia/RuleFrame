package com.ruleframe.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.entity.DictType;
import com.ruleframe.web.mapper.DictTypeMapper;
import com.ruleframe.web.service.DictTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典类型服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictTypeServiceImpl implements DictTypeService {

    private final DictTypeMapper dictTypeMapper;

    @Override
    public Page<DictType> getDictTypePage(int pageNum, int pageSize, String dictName) {
        LambdaQueryWrapper<DictType> queryWrapper = new LambdaQueryWrapper<>();
        if (dictName != null && !dictName.isEmpty()) {
            queryWrapper.like(DictType::getDictName, dictName);
        }
        queryWrapper.orderByAsc(DictType::getSort);
        return dictTypeMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    public java.util.List<DictType> getAllDictTypes() {
        LambdaQueryWrapper<DictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictType::getStatus, 1)
                .orderByAsc(DictType::getSort);
        return dictTypeMapper.selectList(queryWrapper);
    }

    @Override
    public DictType getDictTypeById(Long id) {
        return dictTypeMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictType createDictType(DictType dictType) {
        dictTypeMapper.insert(dictType);
        return dictType;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictType updateDictType(DictType dictType) {
        dictTypeMapper.updateById(dictType);
        return dictType;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictType(Long id) {
        dictTypeMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictTypeStatus(Long id, Integer status) {
        DictType dictType = new DictType();
        dictType.setId(id);
        dictType.setStatus(status);
        dictTypeMapper.updateById(dictType);
    }
}

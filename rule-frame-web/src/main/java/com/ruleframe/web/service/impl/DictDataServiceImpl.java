package com.ruleframe.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.entity.DictData;
import com.ruleframe.web.entity.DictType;
import com.ruleframe.web.mapper.DictDataMapper;
import com.ruleframe.web.mapper.DictTypeMapper;
import com.ruleframe.web.service.DictDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典数据服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictDataServiceImpl implements DictDataService {

    private final DictDataMapper dictDataMapper;
    private final DictTypeMapper dictTypeMapper;

    @Override
    public Page<DictData> getDictDataPage(int pageNum, int pageSize, Long dictTypeId, String dictLabel) {
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictData::getDictTypeId, dictTypeId);
        if (dictLabel != null && !dictLabel.isEmpty()) {
            queryWrapper.like(DictData::getDictLabel, dictLabel);
        }
        queryWrapper.orderByAsc(DictData::getDictSort);
        return dictDataMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    public List<DictData> getDictDataByTypeCode(String dictCode) {
        // 先根据编码查找字典类型
        LambdaQueryWrapper<DictType> typeQuery = new LambdaQueryWrapper<>();
        typeQuery.eq(DictType::getDictCode, dictCode);
        DictType dictType = dictTypeMapper.selectOne(typeQuery);
        if (dictType == null) {
            return List.of();
        }
        // 查询该类型下的所有启用的字典数据
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictData::getDictTypeId, dictType.getId())
                .eq(DictData::getStatus, 1)
                .orderByAsc(DictData::getDictSort);
        return dictDataMapper.selectList(queryWrapper);
    }

    @Override
    public DictData getDictDataById(Long id) {
        return dictDataMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictData createDictData(DictData dictData) {
        dictDataMapper.insert(dictData);
        return dictData;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictData updateDictData(DictData dictData) {
        dictDataMapper.updateById(dictData);
        return dictData;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictData(Long id) {
        dictDataMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictDataStatus(Long id, Integer status) {
        DictData dictData = new DictData();
        dictData.setId(id);
        dictData.setStatus(status);
        dictDataMapper.updateById(dictData);
    }
}

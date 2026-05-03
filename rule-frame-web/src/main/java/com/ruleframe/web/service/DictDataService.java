package com.ruleframe.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.entity.DictData;

import java.util.List;

/**
 * 字典数据服务接口
 */
public interface DictDataService {

    /**
     * 分页查询字典数据（根据字典类型ID）
     */
    Page<DictData> getDictDataPage(int pageNum, int pageSize, Long dictTypeId, String dictLabel);

    /**
     * 根据字典类型编码查询所有启用的字典数据
     */
    List<DictData> getDictDataByTypeCode(String dictCode);

    /**
     * 根据ID查询字典数据
     */
    DictData getDictDataById(Long id);

    /**
     * 创建字典数据
     */
    DictData createDictData(DictData dictData);

    /**
     * 更新字典数据
     */
    DictData updateDictData(DictData dictData);

    /**
     * 删除字典数据
     */
    void deleteDictData(Long id);

    /**
     * 更新字典数据状态
     */
    void updateDictDataStatus(Long id, Integer status);
}

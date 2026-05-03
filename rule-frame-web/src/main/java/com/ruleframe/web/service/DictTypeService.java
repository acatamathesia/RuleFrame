package com.ruleframe.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.entity.DictType;

import java.util.List;

/**
 * 字典类型服务接口
 */
public interface DictTypeService {

    /**
     * 分页查询字典类型
     */
    Page<DictType> getDictTypePage(int pageNum, int pageSize, String dictName);

    /**
     * 查询所有启用的字典类型
     */
    List<DictType> getAllDictTypes();

    /**
     * 根据ID查询字典类型
     */
    DictType getDictTypeById(Long id);

    /**
     * 创建字典类型
     */
    DictType createDictType(DictType dictType);

    /**
     * 更新字典类型
     */
    DictType updateDictType(DictType dictType);

    /**
     * 删除字典类型
     */
    void deleteDictType(Long id);

    /**
     * 更新字典类型状态
     */
    void updateDictTypeStatus(Long id, Integer status);
}

package com.ruleframe.web.rule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.dto.SetElementCreateRequest;
import com.ruleframe.web.dto.SetElementDTO;
import com.ruleframe.web.dto.SetElementQueryRequest;
import com.ruleframe.web.dto.SetElementUpdateRequest;

import java.util.List;

/**
 * 规则元素服务接口
 */
public interface ISetElementService {

    /**
     * 条件分页查询规则元素
     */
    Page<SetElementDTO> pageQuery(SetElementQueryRequest request);

    /**
     * 查询所有规则元素列表
     */
    List<SetElementDTO> listAll();

    /**
     * 根据ID查询
     */
    SetElementDTO getById(Long id);

    /**
     * 创建规则元素
     */
    SetElementDTO create(SetElementCreateRequest request);

    /**
     * 更新规则元素
     */
    SetElementDTO update(SetElementUpdateRequest request);

    /**
     * 删除规则元素
     */
    void delete(Long id);

    /**
     * 批量删除规则元素
     */
    void deleteBatch(List<Long> ids);
}

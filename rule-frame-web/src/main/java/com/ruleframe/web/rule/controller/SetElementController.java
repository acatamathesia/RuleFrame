package com.ruleframe.web.rule.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.dto.*;
import com.ruleframe.web.rule.service.ISetElementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 规则元素控制器
 */
@RestController
@RequestMapping("/api/rule-elements")
@RequiredArgsConstructor
public class SetElementController {

    private final ISetElementService setElementService;

    /**
     * 条件分页查询
     */
    @PostMapping("/page")
    public ApiResponse<PageResult<SetElementDTO>> pageQuery(@RequestBody SetElementQueryRequest request) {
        Page<SetElementDTO> page = setElementService.pageQuery(request);
        PageResult<SetElementDTO> result = PageResult.of(
                page.getRecords(),
                page.getTotal(),
                (int) page.getCurrent(),
                (int) page.getSize());
        return ApiResponse.success(result);
    }

    /**
     * 查询所有规则元素
     */
    @GetMapping("/all")
    public ApiResponse<List<SetElementDTO>> listAll() {
        return ApiResponse.success(setElementService.listAll());
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public ApiResponse<SetElementDTO> getById(@PathVariable Long id) {
        return ApiResponse.success(setElementService.getById(id));
    }

    /**
     * 创建规则元素
     */
    @PostMapping
    public ApiResponse<SetElementDTO> create(@RequestBody SetElementCreateRequest request) {
        return ApiResponse.success(setElementService.create(request));
    }

    /**
     * 更新规则元素
     */
    @PutMapping("/{id}")
    public ApiResponse<SetElementDTO> update(@PathVariable Long id, @RequestBody SetElementUpdateRequest request) {
        request.setId(id);
        return ApiResponse.success(setElementService.update(request));
    }

    /**
     * 删除规则元素
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        setElementService.delete(id);
        return ApiResponse.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/batch")
    public ApiResponse<Void> deleteBatch(@RequestBody List<Long> ids) {
        setElementService.deleteBatch(ids);
        return ApiResponse.success();
    }
}

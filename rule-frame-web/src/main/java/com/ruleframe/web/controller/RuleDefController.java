package com.ruleframe.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.dto.*;
import com.ruleframe.web.rule.service.IRuleDefService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rule-defs")
@RequiredArgsConstructor
public class RuleDefController {

    private final IRuleDefService ruleDefService;

    @GetMapping("/page")
    public ApiResponse<PageResult<RuleDefDTO>> pageQuery(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) Long groupId,
            @RequestParam(required = false) String keyword) {
        Page<RuleDefDTO> page = ruleDefService.pageQuery(pageNum, pageSize, groupId, keyword);
        return ApiResponse.success(PageResult.of(
            page.getRecords(), page.getTotal(),
            (int) page.getCurrent(), (int) page.getSize()));
    }

    @GetMapping("/group/{groupCode}")
    public ApiResponse<List<RuleDefDTO>> listByGroupCode(@PathVariable String groupCode) {
        return ApiResponse.success(ruleDefService.listByGroupCode(groupCode));
    }

    @GetMapping("/{id}")
    public ApiResponse<RuleDefDTO> getById(@PathVariable Long id) {
        return ApiResponse.success(ruleDefService.getById(id));
    }

    @PostMapping
    public ApiResponse<RuleDefDTO> create(@RequestBody RuleDefCreateRequest request) {
        return ApiResponse.success(ruleDefService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<RuleDefDTO> update(@PathVariable Long id, @RequestBody RuleDefUpdateRequest request) {
        request.setId(id);
        return ApiResponse.success(ruleDefService.update(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        ruleDefService.delete(id);
        return ApiResponse.success();
    }

    @DeleteMapping("/batch")
    public ApiResponse<Void> deleteBatch(@RequestBody List<Long> ids) {
        ruleDefService.deleteBatch(ids);
        return ApiResponse.success();
    }
}

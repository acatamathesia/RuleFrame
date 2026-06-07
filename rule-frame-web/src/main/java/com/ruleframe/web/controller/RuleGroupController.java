package com.ruleframe.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.dto.*;
import com.ruleframe.web.rule.service.IRuleGroupDefService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rule-groups")
@RequiredArgsConstructor
public class RuleGroupController {

    private final IRuleGroupDefService ruleGroupDefService;

    @GetMapping("/page")
    public ApiResponse<PageResult<RuleGroupDTO>> pageQuery(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword) {
        Page<RuleGroupDTO> page = ruleGroupDefService.pageQuery(pageNum, pageSize, keyword);
        return ApiResponse.success(PageResult.of(
            page.getRecords(), page.getTotal(),
            (int) page.getCurrent(), (int) page.getSize()));
    }

    @GetMapping("/all")
    public ApiResponse<List<RuleGroupDTO>> listAll() {
        return ApiResponse.success(ruleGroupDefService.listAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<RuleGroupDTO> getById(@PathVariable Long id) {
        return ApiResponse.success(ruleGroupDefService.getById(id));
    }

    @PostMapping
    public ApiResponse<RuleGroupDTO> create(@RequestBody RuleGroupCreateRequest request) {
        return ApiResponse.success(ruleGroupDefService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<RuleGroupDTO> update(@PathVariable Long id, @RequestBody RuleGroupUpdateRequest request) {
        request.setId(id);
        return ApiResponse.success(ruleGroupDefService.update(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        ruleGroupDefService.delete(id);
        return ApiResponse.success();
    }

    @DeleteMapping("/batch")
    public ApiResponse<Void> deleteBatch(@RequestBody List<Long> ids) {
        ruleGroupDefService.deleteBatch(ids);
        return ApiResponse.success();
    }
}

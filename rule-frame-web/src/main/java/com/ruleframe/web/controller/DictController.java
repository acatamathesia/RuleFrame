package com.ruleframe.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.dto.ApiResponse;
import com.ruleframe.web.entity.DictData;
import com.ruleframe.web.entity.DictType;
import com.ruleframe.web.service.DictDataService;
import com.ruleframe.web.service.DictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 字典管理控制器
 */
@RestController
@RequiredArgsConstructor
public class DictController {

    private final DictTypeService dictTypeService;
    private final DictDataService dictDataService;

    // ========== 字典类型（父字典）接口 ==========

    /**
     * 分页查询字典类型
     */
    @GetMapping("/api/dict-types/page")
    public ApiResponse<Page<DictType>> getDictTypePage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String dictName) {
        Page<DictType> page = dictTypeService.getDictTypePage(pageNum, pageSize, dictName);
        return ApiResponse.success(page);
    }

    /**
     * 查询所有启用的字典类型
     */
    @GetMapping("/api/dict-types/all")
    public ApiResponse<List<DictType>> getAllDictTypes() {
        List<DictType> list = dictTypeService.getAllDictTypes();
        return ApiResponse.success(list);
    }

    /**
     * 根据ID查询字典类型
     */
    @GetMapping("/api/dict-types/{id}")
    public ApiResponse<DictType> getDictTypeById(@PathVariable Long id) {
        DictType dictType = dictTypeService.getDictTypeById(id);
        if (dictType == null) {
            return ApiResponse.error("字典类型不存在");
        }
        return ApiResponse.success(dictType);
    }

    /**
     * 创建字典类型
     */
    @PostMapping("/api/dict-types")
    public ApiResponse<DictType> createDictType(@RequestBody DictType dictType) {
        DictType created = dictTypeService.createDictType(dictType);
        return ApiResponse.success(created);
    }

    /**
     * 更新字典类型
     */
    @PutMapping("/api/dict-types/{id}")
    public ApiResponse<DictType> updateDictType(@PathVariable Long id, @RequestBody DictType dictType) {
        dictType.setId(id);
        DictType updated = dictTypeService.updateDictType(dictType);
        return ApiResponse.success(updated);
    }

    /**
     * 删除字典类型
     */
    @DeleteMapping("/api/dict-types/{id}")
    public ApiResponse<Void> deleteDictType(@PathVariable Long id) {
        dictTypeService.deleteDictType(id);
        return ApiResponse.success(null);
    }

    /**
     * 更新字典类型状态
     */
    @PutMapping("/api/dict-types/{id}/status")
    public ApiResponse<Void> updateDictTypeStatus(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        Integer status = request.get("status");
        dictTypeService.updateDictTypeStatus(id, status);
        return ApiResponse.success(null);
    }

    // ========== 字典数据（子字典）接口 ==========

    /**
     * 分页查询字典数据
     */
    @GetMapping("/api/dict-data/page")
    public ApiResponse<Page<DictData>> getDictDataPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam Long dictTypeId,
            @RequestParam(required = false) String dictLabel) {
        Page<DictData> page = dictDataService.getDictDataPage(pageNum, pageSize, dictTypeId, dictLabel);
        return ApiResponse.success(page);
    }

    /**
     * 根据字典类型编码查询字典数据
     */
    @GetMapping("/api/dict-data/type/{dictCode}")
    public ApiResponse<List<DictData>> getDictDataByTypeCode(@PathVariable String dictCode) {
        List<DictData> list = dictDataService.getDictDataByTypeCode(dictCode);
        return ApiResponse.success(list);
    }

    /**
     * 根据ID查询字典数据
     */
    @GetMapping("/api/dict-data/{id}")
    public ApiResponse<DictData> getDictDataById(@PathVariable Long id) {
        DictData dictData = dictDataService.getDictDataById(id);
        if (dictData == null) {
            return ApiResponse.error("字典数据不存在");
        }
        return ApiResponse.success(dictData);
    }

    /**
     * 创建字典数据
     */
    @PostMapping("/api/dict-data")
    public ApiResponse<DictData> createDictData(@RequestBody DictData dictData) {
        DictData created = dictDataService.createDictData(dictData);
        return ApiResponse.success(created);
    }

    /**
     * 更新字典数据
     */
    @PutMapping("/api/dict-data/{id}")
    public ApiResponse<DictData> updateDictData(@PathVariable Long id, @RequestBody DictData dictData) {
        dictData.setId(id);
        DictData updated = dictDataService.updateDictData(dictData);
        return ApiResponse.success(updated);
    }

    /**
     * 删除字典数据
     */
    @DeleteMapping("/api/dict-data/{id}")
    public ApiResponse<Void> deleteDictData(@PathVariable Long id) {
        dictDataService.deleteDictData(id);
        return ApiResponse.success(null);
    }

    /**
     * 更新字典数据状态
     */
    @PutMapping("/api/dict-data/{id}/status")
    public ApiResponse<Void> updateDictDataStatus(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        Integer status = request.get("status");
        dictDataService.updateDictDataStatus(id, status);
        return ApiResponse.success(null);
    }
}

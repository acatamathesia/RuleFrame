package com.ruleframe.web.dto;

import lombok.Data;

/**
 * 规则元素分页查询请求
 */
@Data
public class SetElementQueryRequest {

    /**
     * 关键词（元素名称/路径模糊搜索）
     */
    private String keyword;

    /**
     * 数据类型筛选
     */
    private String dataType;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}

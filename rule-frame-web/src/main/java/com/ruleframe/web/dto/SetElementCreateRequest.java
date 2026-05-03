package com.ruleframe.web.dto;

import lombok.Data;

/**
 * 创建规则元素请求
 */
@Data
public class SetElementCreateRequest {

    /**
     * 元素编码
     */
    private String code;

    /**
     * 元素名称
     */
    private String name;

    /**
     * 元素路径（JSONPath表达式）
     */
    private String elPath;

    /**
     * 是否类型转换：0-否，1-是
     */
    private Integer converted;

    /**
     * 数据类型：string-字符串，number-数字，boolean-布尔，date-日期
     */
    private String convertType;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer enabled;
}

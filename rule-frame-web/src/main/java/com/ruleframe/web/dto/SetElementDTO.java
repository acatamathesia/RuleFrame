package com.ruleframe.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 规则元素DTO（前端展示用）
 */
@Data
public class SetElementDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 元素编码
     */
    private String code;

    /**
     * 元素名称
     */
    private String elementName;

    /**
     * 元素路径
     */
    private String elementPath;

    /**
     * 是否类型转换
     */
    private Boolean needConvert;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

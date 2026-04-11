package com.ruleframe.core.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class FieldDefinition {
    /**
     * 解析路径
     * 数据格式，默认字符串
     * 
     */
    private String fieldName;
    private String fieldPath;
    // 是否需要进行数据类型的转换
    private String isConvert;
    private String dataType;

}

package com.ruleframe.core.handler;

import com.ruleframe.core.param.FieldConvertResult;
import com.ruleframe.core.param.FieldDefinition;

/**
 * 字段数据类型转换器
 */
public interface FieldConvertHandler {

    /**
     * 通过字段定义的格式转换成指定数据
     * @param dataDefinition 字段定义内容
     * @return 类型转换结果
     */
    FieldConvertResult convert(FieldDefinition dataDefinition);

    /**
     * 1. 配置字段内容
     * 2. 找到字段内容，合成字段配置器
     * 3. 根据字段内容将结果转
     */


}

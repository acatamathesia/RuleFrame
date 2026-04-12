package com.ruleframe.core.converter;

import java.util.regex.Pattern;

import com.ruleframe.exception.ConversionException;

@AutoRegisterConverter
public class DoubleConverter implements ValueConverter {

    private final String TYPE_NAME = "to_double";
    private final Pattern DOUBLE_PATTERN = Pattern.compile("^[-+]?\\d+(\\.\\d+)?$");

    @Override
    public String getType() {
        return TYPE_NAME;
    }

    @Override
    public Object convert(Object rawValue) throws ConversionException {
        // 有限匹配数据类型
        if (rawValue instanceof Number) {
            return ((Number) rawValue).doubleValue();
        }
        // 判断是否是字符串
        if (rawValue instanceof String && DOUBLE_PATTERN.matcher((String)rawValue).matches()) {
            return Double.valueOf((String) rawValue);
        }
        throw new ConversionException("转换器: ["+TYPE_NAME+"], 执行失败, 原始数据不是数字类型. 原始数据: " + rawValue);
    }
    
}

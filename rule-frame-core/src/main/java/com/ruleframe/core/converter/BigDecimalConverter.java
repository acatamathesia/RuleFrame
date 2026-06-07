package com.ruleframe.core.converter;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ruleframe.exception.ConversionException;

@AutoRegisterConverter
public class BigDecimalConverter implements ValueConverter {

    private final String TYPE_NAME = "to_number";
    private final Pattern NUMBER_PATTERN = Pattern.compile("^[-+]?\\d+(\\.\\d+)?$");

    @Override
    public String getType() {
        return TYPE_NAME;
    }

    @Override
    public Object convert(Object rawValue) throws ConversionException {
        if (rawValue instanceof List) {
            return convertListData((List<?>) rawValue);
        }
        return simpleConvert(rawValue);
    }

    private Object convertListData(List<?> rawValue) {
        List<Object> resultList = rawValue.stream().map(this::simpleConvert).collect(Collectors.toList());
        if (resultList.stream().anyMatch(p -> p == null)) {
            throw new ConversionException("转换器: [" + TYPE_NAME + "], 执行失败, 原始数据不是数字类型. 原始数据: " + rawValue);
        }
        return resultList;
    }

    private Object simpleConvert(Object rawValue) {
        // 优先匹配数据类型
        if (rawValue instanceof BigDecimal) {
            return (BigDecimal) rawValue;
        }
        if (rawValue instanceof Number) {
            return new BigDecimal(rawValue.toString());
        }
        // 判断是否是字符串
        if (rawValue instanceof String && NUMBER_PATTERN.matcher((String) rawValue).matches()) {
            return new BigDecimal((String) rawValue);
        }
        return null;
    }

}

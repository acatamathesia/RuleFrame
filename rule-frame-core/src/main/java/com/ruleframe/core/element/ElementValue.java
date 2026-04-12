package com.ruleframe.core.element;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElementValue {
    
    private final boolean success;
    private final Object rawValue;
    private final Object convertedValue;
    private final String errMessage;

    public static ElementValue success(Object rawValue, Object convertedValue) {
        return new ElementValue(true, rawValue, convertedValue, "");
    }

    public static ElementValue failure(Object rawValue, String errMessage) {
        return new ElementValue(false, rawValue, null, errMessage);
    }

    public static ElementValue failure(Object rawValue) {
        return failure(rawValue, "数据类型转换失败, 原始数据: "+rawValue);
    }

}

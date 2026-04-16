package com.ruleframe.core.element;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElementValue {
    
    private final boolean success;
    private final Object rawValue;
    private final Object convertedValue;
    private final Class<?> clzz;
    private final String errMessage;

    public static ElementValue success(Object rawValue, Object convertedValue, Class<?> clzz) {
        return new ElementValue(true, rawValue, convertedValue, clzz, "");
    }

    public static ElementValue failure(Object rawValue, String errMessage) {
        return new ElementValue(false, rawValue, null, null, errMessage);
    }

    /**
     * 判断 convertedValue 是否为指定类型
     */
    public boolean isType(Class<?> targetType) {
        return clzz != null && targetType.isAssignableFrom(clzz);
    }

    /**
     * 安全地将 convertedValue 转换为指定类型
     */
    @SuppressWarnings("unchecked")
    public <T> T getValueAs(Class<T> targetType) {
        if (convertedValue == null) {
            return null;
        }
        if (clzz != null && targetType.isAssignableFrom(clzz)) {
            return (T) convertedValue;
        }
        throw new ClassCastException("无法将 " + (clzz != null ? clzz.getName() : "unknown") + " 转换为 " + targetType.getName());
    }

    public static ElementValue failure(Object rawValue) {
        return failure(rawValue, "数据类型转换失败, 原始数据: "+rawValue);
    }

}

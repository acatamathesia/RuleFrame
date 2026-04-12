package com.ruleframe.core.converter;

import com.ruleframe.exception.ConversionException;

public interface ValueConverter {
    String getType(); // 例如 "TO_INT", "TO_DECIMAL:2", "DATE:yyyy-MM-dd"
    Object convert(Object rawValue) throws ConversionException;
}

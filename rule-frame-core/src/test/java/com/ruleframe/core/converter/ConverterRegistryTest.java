package com.ruleframe.core.converter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 转换器自动注册测试
 */
public class ConverterRegistryTest {

    @Test
    public void testAutoRegisterConverters() {
        // 测试自动注册的转换器
        System.out.println("已注册的转换器类型: " + ConverterRegistry.getRegisteredTypes());
        
        // 验证 IntegerConverter 是否被自动注册
        assertTrue(ConverterRegistry.hasConverter("to_int"), "应该包含 to_int 转换器");
        
        // 验证 BigDecimalConverter 是否被自动注册
        assertTrue(ConverterRegistry.hasConverter("to_bigdecimal"), "应该包含 to_bigdecimal 转换器");
    }

    @Test
    public void testBigDecimalConverter() {
        ValueConverter converter = ConverterRegistry.getConverter("to_bigdecimal");
        assertNotNull(converter, "应该能找到 to_bigdecimal 转换器");
        
        // 测试转换
        assertNotNull(converter.convert("123.45"));
        assertNotNull(converter.convert(456));
        assertNull(converter.convert(null));
    }

    @Test
    public void testManualRegister() {
        // 测试手动注册
        String customType = "custom_converter";
        assertFalse(ConverterRegistry.hasConverter(customType));
        
        ConverterRegistry.registerConverter(customType, new ValueConverter() {
            @Override
            public String getType() {
                return customType;
            }

            @Override
            public Object convert(Object rawValue) {
                return "converted: " + rawValue;
            }
        });
        
        assertTrue(ConverterRegistry.hasConverter(customType));
        ValueConverter converter = ConverterRegistry.getConverter(customType);
        assertNotNull(converter);
        assertEquals("converted: test", converter.convert("test"));
    }
}

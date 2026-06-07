package com.ruleframe.core.element;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ElementValue 单元测试 测试工厂方法、类型判断和类型安全转换
 */
public class ElementValueTest {

    @Test
    public void testSuccessFactory() {
        ElementValue value = ElementValue.success("raw123", 123, Integer.class);

        assertTrue(value.isSuccess(), "success工厂创建的对象应标记为成功");
        assertEquals("raw123", value.getRawValue());
        assertEquals(123, value.getConvertedValue());
        assertEquals(Integer.class, value.getClzz());
        assertEquals("", value.getErrMessage());
    }

    @Test
    public void testFailureWithMessage() {
        ElementValue value = ElementValue.failure("bad_data", "类型转换错误");

        assertFalse(value.isSuccess(), "failure工厂创建的对象应标记为失败");
        assertEquals("bad_data", value.getRawValue());
        assertNull(value.getConvertedValue());
        assertNull(value.getClzz());
        assertEquals("类型转换错误", value.getErrMessage());
    }

    @Test
    public void testFailureWithoutMessage() {
        ElementValue value = ElementValue.failure("bad_data");

        assertFalse(value.isSuccess());
        assertTrue(value.getErrMessage().contains("数据类型转换失败"), "默认错误信息应包含'数据类型转换失败'");
        assertTrue(value.getErrMessage().contains("bad_data"), "默认错误信息应包含原始数据");
    }

    @Test
    public void testIsTypeMatch() {
        ElementValue value = ElementValue.success("42", 42, Integer.class);
        assertTrue(value.isType(Integer.class), "Integer.class应匹配Integer类型");
        assertTrue(value.isType(Number.class), "Number.class是Integer的父类, 应匹配");
        assertTrue(value.isType(Object.class), "Object.class是所有类的父类, 应匹配");
    }

    @Test
    public void testIsTypeMismatch() {
        ElementValue value = ElementValue.success("42", 42, Integer.class);
        assertFalse(value.isType(String.class), "String.class不应匹配Integer类型");
        assertFalse(value.isType(Double.class), "Double.class不应匹配Integer类型");
    }

    @Test
    public void testIsTypeNullClass() {
        ElementValue value = ElementValue.failure("x", "error");
        assertFalse(value.isType(Object.class), "clzz为null时isType应返回false");
    }

    @Test
    public void testGetValueAsCorrectType() {
        ElementValue value = ElementValue.success("42", 42, Integer.class);

        Integer result = value.getValueAs(Integer.class);
        assertEquals(42, result);
    }

    @Test
    public void testGetValueAsNullValue() {
        ElementValue value = ElementValue.failure("x", "error");
        // convertedValue is null in failure
        assertNull(value.getValueAs(String.class), "convertedValue为null时应返回null");
    }

    @Test
    public void testGetValueAsWrongTypeThrows() {
        ElementValue value = ElementValue.success("42", 42, Integer.class);

        assertThrows(ClassCastException.class, () -> value.getValueAs(String.class),
                "转换为不兼容类型应抛出ClassCastException");
    }

    @Test
    public void testSuccessWithNullValues() {
        ElementValue value = ElementValue.success(null, null, null);

        assertTrue(value.isSuccess());
        assertNull(value.getRawValue());
        assertNull(value.getConvertedValue());
        assertNull(value.getClzz());
    }
}

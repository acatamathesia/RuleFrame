package com.ruleframe.core.fact;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

/**
 * MapFactContext 单元测试 测试JSON扁平化后的事实上下文取值
 */
public class MapFactContextTest {

    @Test
    public void testSimpleJsonParsing() {
        String json = "{\"name\":\"Alice\",\"age\":\"30\"}";
        MapFactContext ctx = new MapFactContext(json);

        assertEquals("Alice", ctx.getValue("name"), "应能通过路径获取简单JSON值");
        assertEquals("30", ctx.getValue("age"));
    }

    @Test
    public void testNestedJsonParsing() {
        String json = "{\"user\":{\"name\":\"Bob\",\"city\":\"NYC\"}}";
        MapFactContext ctx = new MapFactContext(json);

        // JsonFallternParser使用 "/" 作为嵌套对象的路径分隔符
        assertEquals("Bob", ctx.getValue("user/name"), "应能通过 'user/name' 路径获取嵌套JSON的值");
        assertEquals("NYC", ctx.getValue("user/city"), "应能通过 'user/city' 路径获取嵌套JSON的值");
    }

    @Test
    public void testNonExistentPath() {
        String json = "{\"name\":\"Alice\"}";
        MapFactContext ctx = new MapFactContext(json);

        assertNull(ctx.getValue("nonexistent"), "不存在的路径应返回null");
    }

    @Test
    public void testHasFact() {
        String json = "{\"status\":\"active\",\"count\":\"5\"}";
        MapFactContext ctx = new MapFactContext(json);

        assertTrue(ctx.hasFact("status"), "存在的字段应返回true");
        assertFalse(ctx.hasFact("missing"), "不存在的字段应返回false");
    }

    @Test
    public void testGetFactNames() {
        String json = "{\"a\":\"1\",\"b\":\"2\",\"c\":\"3\"}";
        MapFactContext ctx = new MapFactContext(json);

        Set<String> names = ctx.getFactNames();
        assertNotNull(names);
        assertEquals(3, names.size());
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
        assertTrue(names.contains("c"));
    }

    @Test
    public void testEmptyJson() {
        String json = "{}";
        MapFactContext ctx = new MapFactContext(json);

        // 空JSON: getValue应返回null（因为 PARSER_MAP.isEmpty()）
        assertNull(ctx.getValue("anything"), "空JSON不应有任何值");
        assertTrue(ctx.getFactNames().isEmpty(), "空JSON的事实名称应为空");
    }

    @Test
    public void testInvalidJson() {
        assertThrows(RuntimeException.class, () -> new MapFactContext("not valid json"),
                "无效JSON应抛出RuntimeException");
    }

    @Test
    public void testJsonArray() {
        String json = "[{\"id\":\"1\"},{\"id\":\"2\"}]";
        MapFactContext ctx = new MapFactContext(json);

        // 数组会被扁平化，元素索引合并到key中
        assertNotNull(ctx.getFactNames(), "数组JSON应能解析");
        assertFalse(ctx.getFactNames().isEmpty(), "数组JSON应有事实名称");
    }
}

package com.ruleframe.core.condition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ConditionResult 单元测试 测试条件结果的创建、状态判断和元数据操作
 */
public class ConditionResultTest {

    @Test
    public void testSuccess() {
        ConditionResult result = ConditionResult.success();

        assertTrue(result.isPassed(), "success()创建的应标记为通过");
        assertNull(result.getFailureReason(), "成功时失败原因应为null");
    }

    @Test
    public void testFailure() {
        ConditionResult result = ConditionResult.failure("条件不满足");

        assertFalse(result.isPassed(), "failure()创建的应标记为失败");
        assertEquals("条件不满足", result.getFailureReason());
    }

    @Test
    public void testMetadataSetAndGet() {
        ConditionResult result = ConditionResult.success();

        result.setMetadata("key1", "value1");
        result.setMetadata("key2", 42);

        assertEquals("value1", result.getMetadata("key1"));
        assertEquals(42, result.getMetadata("key2"));
    }

    @Test
    public void testMetadataGetNonExistent() {
        ConditionResult result = ConditionResult.success();
        assertNull(result.getMetadata("nonexistent"), "不存在的键应返回null");
    }

    @Test
    public void testSetAllMetadata() {
        ConditionResult result = ConditionResult.success();
        result.setMetadata("existing", "old");

        java.util.Map<String, Object> newMetadata = new java.util.HashMap<>();
        newMetadata.put("keyA", "valA");
        newMetadata.put("keyB", 100);

        result.setAllMetadata(newMetadata);

        assertEquals("valA", result.getMetadata("keyA"));
        assertEquals(100, result.getMetadata("keyB"));
        assertEquals("old", result.getMetadata("existing"), "原有元数据应保留");
    }

    @Test
    public void testListMetaDataKeys() {
        ConditionResult result = ConditionResult.success();
        result.setMetadata("a", 1);
        result.setMetadata("b", 2);

        java.util.Set<String> keys = result.listMetaDataKeys();
        assertEquals(2, keys.size());
        assertTrue(keys.contains("a"));
        assertTrue(keys.contains("b"));
    }

    @Test
    public void testListMetaDataKeysEmpty() {
        ConditionResult result = ConditionResult.success();
        java.util.Set<String> keys = result.listMetaDataKeys();
        assertTrue(keys.isEmpty(), "无元数据时应返回空集合");
    }

    @Test
    public void testToString() {
        ConditionResult result = ConditionResult.success();
        result.setMetadata("code", "S001");
        // toString 不应抛异常
        assertNotNull(result.toString());
    }
}

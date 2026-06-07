package com.ruleframe.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JsonUtil 单元测试 测试 ObjectMapper 单例获取
 */
public class JsonUtilTest {

    @Test
    public void testGetObjectMapper() {
        ObjectMapper om = JsonUtil.getObjectMapper();
        assertNotNull(om, "ObjectMapper不应为null");
    }

    @Test
    public void testSingletonBehavior() {
        ObjectMapper om1 = JsonUtil.getObjectMapper();
        ObjectMapper om2 = JsonUtil.getObjectMapper();

        assertSame(om1, om2, "多次调用应返回同一实例");
    }

    @Test
    public void testStaticFieldAccess() {
        ObjectMapper om = JsonUtil.om;
        assertNotNull(om, "静态字段om不应为null");
        assertSame(om, JsonUtil.getObjectMapper(), "静态字段和getObjectMapper应返回同一实例");
    }
}

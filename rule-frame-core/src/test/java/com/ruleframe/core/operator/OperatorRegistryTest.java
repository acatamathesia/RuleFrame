package com.ruleframe.core.operator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * OperatorRegistry 单元测试 测试运算符的自动注册和手动注册
 */
public class OperatorRegistryTest {

    @Test
    public void testAutoRegisterOperators() {
        // 验证 = 运算符被自动注册
        assertTrue(OperatorRegistry.hasOperator("="), "应包含 '=' 运算符（EqualOperator）");

        // 验证 > 运算符被自动注册
        assertTrue(OperatorRegistry.hasOperator(">"), "应包含 '>' 运算符（GreaterThanOperator）");

        // 验证 < 运算符被自动注册
        assertTrue(OperatorRegistry.hasOperator("<"), "应包含 '<' 运算符（LessThanOperator）");

        // 验证 >= 运算符被自动注册
        assertTrue(OperatorRegistry.hasOperator(">="), "应包含 '>=' 运算符");
    }

    @Test
    public void testGetOperator() {
        Operator eq = OperatorRegistry.getOperator("=");
        assertNotNull(eq, "应能获取 '=' 运算符");
        assertTrue(eq.apply(5, 5), "等于运算符应工作正常");
    }

    @Test
    public void testGetNonExistentOperator() {
        assertNull(OperatorRegistry.getOperator("no_such_operator"), "不存在的运算符应返回null");
    }

    @Test
    public void testManualRegister() {
        String opName = "custom_test_op";

        assertFalse(OperatorRegistry.hasOperator(opName), "手动注册前不应存在");

        OperatorRegistry.registerOperator(opName, (factValue, expectedValue) -> true);

        assertTrue(OperatorRegistry.hasOperator(opName), "手动注册后应存在");

        Operator customOp = OperatorRegistry.getOperator(opName);
        assertNotNull(customOp);
        assertTrue(customOp.apply("anything", "else"), "自定义运算符应工作正常");
    }

    @Test
    public void testGetRegisteredNames() {
        java.util.Set<String> names = OperatorRegistry.getRegisteredNames();
        assertNotNull(names);
        assertFalse(names.isEmpty(), "应至少注册了一些运算符");
        assertTrue(names.contains("="), "应包含 '=' 运算符");
    }
}

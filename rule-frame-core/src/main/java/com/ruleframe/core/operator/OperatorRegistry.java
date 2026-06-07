package com.ruleframe.core.operator;

import com.ruleframe.utils.AnnotationScannerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 运算符的注册表，支持自动扫描和注册 使用 @AutoRegisterOperator 注解标记的运算符会自动被注册
 */
public class OperatorRegistry {

    private static final Logger logger = LoggerFactory.getLogger(OperatorRegistry.class);

    /**
     * 运算符所在的包路径
     */
    private static final String OPERATOR_PACKAGE = "com.ruleframe.core.operator";

    /**
     * 运算符名称别名映射：将外部配置中的名称映射为内部注册的运算符名称
     */
    private static final Map<String, String> ALIAS_MAP = Map.of(
            "GREATER_THAN", ">",
            "GREATER_EQUAL", ">=",
            "LESS_THAN", "<",
            "EQUAL", "=",
            "NOT_EQUAL", "!=",
            "LESS_EQUAL", "<=");

    /**
     * 使用通用注解扫描注册器
     */
    private static final AnnotationScannerRegistry<Operator> registry;

    static {
        // 构建并初始化扫描注册器
        registry = new AnnotationScannerRegistry.ScannerConfig<Operator>()
                .name("OperatorRegistry")
                .packageName(OPERATOR_PACKAGE)
                .targetType(Operator.class)
                .annotationClass(AutoRegisterOperator.class)
                .instanceFactory(clazz -> {
                    try {
                        return (Operator) clazz.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("实例化运算符失败: " + clazz.getName(), e);
                    }
                })
                .keyExtractor((clazz, operator) -> {
                    AutoRegisterOperator annotation = clazz.getAnnotation(AutoRegisterOperator.class);
                    return annotation.value();
                })
                .build();
    }

    /**
     * 根据名称获取运算符，支持别名映射
     */
    public static Operator getOperator(String name) {
        if (name == null)
            return null;
        Operator op = registry.get(name);
        if (op == null) {
            String mappedName = ALIAS_MAP.get(name);
            if (mappedName != null) {
                op = registry.get(mappedName);
            }
        }
        return op;
    }

    /**
     * 注册运算符
     */
    public static void registerOperator(String name, Operator operator) {
        registry.register(name, operator);
    }

    /**
     * 检查是否包含指定名称的运算符
     */
    public static boolean hasOperator(String name) {
        if (name == null)
            return false;
        return registry.has(name) || ALIAS_MAP.containsKey(name);
    }

    /**
     * 获取所有已注册的运算符名称
     */
    public static java.util.Set<String> getRegisteredNames() {
        return registry.getRegisteredKeys();
    }
}

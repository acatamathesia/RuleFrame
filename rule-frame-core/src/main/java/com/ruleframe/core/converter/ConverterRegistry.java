package com.ruleframe.core.converter;

import com.ruleframe.utils.AnnotationScannerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 转换器的注册表，支持自动扫描和注册
 * 使用 @AutoRegisterConverter 注解标记的转换器会自动被注册
 */
public class ConverterRegistry {
    
    private static final Logger logger = LoggerFactory.getLogger(ConverterRegistry.class);
    
    /**
     * 转换器所在的包路径
     */
    private static final String CONVERTER_PACKAGE = "com.ruleframe.core.converter";
    
    /**
     * 使用通用注解扫描注册器
     */
    private static final AnnotationScannerRegistry<ValueConverter> registry;
    
    static {
        // 构建并初始化扫描注册器
        registry = new AnnotationScannerRegistry.ScannerConfig<ValueConverter>()
            .name("ConverterRegistry")
            .packageName(CONVERTER_PACKAGE)
            .targetType(ValueConverter.class)
            .annotationClass(AutoRegisterConverter.class)
            .instanceFactory(clazz -> {
                try {
                    return (ValueConverter) clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("实例化转换器失败: " + clazz.getName(), e);
                }
            })
            .keyExtractor((clazz, converter) -> {
                AutoRegisterConverter annotation = clazz.getAnnotation(AutoRegisterConverter.class);
                return annotation.value().isEmpty() ? converter.getType() : annotation.value();
            })
            .build();
    }
    
    /**
     * 根据类型获取转换器
     */
    public static ValueConverter getConverter(String type) {
        return registry.get(type);
    }
    
    /**
     * 注册转换器
     */
    public static void registerConverter(String type, ValueConverter converter) {
        registry.register(type, converter);
    }
    
    /**
     * 检查是否包含指定类型的转换器
     */
    public static boolean hasConverter(String type) {
        return registry.has(type);
    }
    
    /**
     * 获取所有已注册的转换器类型
     */
    public static java.util.Set<String> getRegisteredTypes() {
        return registry.getRegisteredKeys();
    }
    
    /**
     * 清空所有注册的转换器
     */
    public static void clear() {
        registry.clear();
    }
}

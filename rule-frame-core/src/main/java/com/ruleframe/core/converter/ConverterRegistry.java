package com.ruleframe.core.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 转换器的注册表，支持自动扫描和注册
 * 使用 @AutoRegisterConverter 注解标记的转换器会自动被注册
 */
public class ConverterRegistry {
    
    private static final Logger logger = LoggerFactory.getLogger(ConverterRegistry.class);
    
    /**
     * 存储所有注册的转换器
     */
    private static final Map<String, ValueConverter> converterMap = new ConcurrentHashMap<>();
    
    /**
     * 转换器所在的包路径
     */
    private static final String CONVERTER_PACKAGE = "com.ruleframe.core.converter";
    
    static {
        // 在静态代码块中自动扫描并注册转换器
        autoScanAndRegister();
    }
    
    /**
     * 自动扫描并注册所有带有 @AutoRegisterConverter 注解的转换器
     */
    private static void autoScanAndRegister() {
        try {
            List<Class<?>> converterClasses = scanConverterClasses();
            for (Class<?> clazz : converterClasses) {
                if (ValueConverter.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                    registerConverterFromClass(clazz);
                }
            }
            logger.info("自动注册完成，共注册 {} 个转换器", converterMap.size());
        } catch (Exception e) {
            logger.error("自动扫描注册转换器失败", e);
        }
    }
    
    /**
     * 扫描包下所有的类
     */
    private static List<Class<?>> scanConverterClasses() throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        String packageName = CONVERTER_PACKAGE;
        String path = packageName.replace('.', '/');
        
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());
                if (directory.exists() && directory.isDirectory()) {
                    classes.addAll(findClasses(directory, packageName));
                }
            }
        } catch (IOException e) {
            logger.error("扫描转换器包失败: {}", path, e);
        }
        
        return classes;
    }
    
    /**
     * 在目录中查找所有的类
     */
    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        
        File[] files = directory.listFiles();
        if (files == null) {
            return classes;
        }
        
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + 
                    file.getName().substring(0, file.getName().length() - 6);
                classes.add(Class.forName(className));
            }
        }
        
        return classes;
    }
    
    /**
     * 从类定义注册转换器
     */
    private static void registerConverterFromClass(Class<?> clazz) {
        try {
            // 检查是否有 @AutoRegisterConverter 注解
            AutoRegisterConverter annotation = clazz.getAnnotation(AutoRegisterConverter.class);
            if (annotation == null) {
                return; // 没有注解，不注册
            }
            
            // 创建实例
            ValueConverter converter = (ValueConverter) clazz.getDeclaredConstructor().newInstance();
            
            // 获取类型名称
            String type = annotation.value().isEmpty() ? converter.getType() : annotation.value();
            
            // 注册
            converterMap.put(type, converter);
            logger.debug("自动注册转换器: {} -> {}", type, clazz.getSimpleName());
        } catch (Exception e) {
            logger.error("注册转换器失败: {}", clazz.getName(), e);
        }
    }
    
    /**
     * 根据类型获取转换器
     */
    public static ValueConverter getConverter(String type) {
        return converterMap.get(type);
    }
    
    /**
     * 注册转换器
     */
    public static void registerConverter(String type, ValueConverter converter) {
        converterMap.put(type, converter);
        logger.debug("手动注册转换器: {} -> {}", type, converter.getClass().getSimpleName());
    }
    
    /**
     * 检查是否包含指定类型的转换器
     */
    public static boolean hasConverter(String type) {
        return converterMap.containsKey(type);
    }
    
    /**
     * 获取所有已注册的转换器类型
     */
    public static java.util.Set<String> getRegisteredTypes() {
        return converterMap.keySet();
    }
    
    /**
     * 清空所有注册的转换器
     */
    public static void clear() {
        converterMap.clear();
    }
}

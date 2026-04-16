package com.ruleframe.utils;

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
import java.util.function.Function;

/**
 * 通用的注解扫描注册工具类
 * 支持自动扫描指定包下带有特定注解的类，并注册到映射表中
 * 
 * @param <T> 注册的实例类型
 */
public class AnnotationScannerRegistry<T> {
    
    private static final Logger logger = LoggerFactory.getLogger(AnnotationScannerRegistry.class);
    
    /**
     * 存储所有注册的实例
     */
    private final Map<String, T> registryMap = new ConcurrentHashMap<>();
    
    /**
     * 扫描器配置
     */
    private final ScannerConfig<T> config;
    
    /**
     * 构造函数
     * 
     * @param config 扫描器配置
     */
    public AnnotationScannerRegistry(ScannerConfig<T> config) {
        this.config = config;
        if (config.isAutoScan()) {
            autoScanAndRegister();
        }
    }
    
    /**
     * 自动扫描并注册所有带有指定注解的类
     */
    private void autoScanAndRegister() {
        try {
            List<Class<?>> classes = scanClasses(config.getPackageName());
            for (Class<?> clazz : classes) {
                if (config.getTargetType().isAssignableFrom(clazz) && !clazz.isInterface()) {
                    registerFromClass(clazz);
                }
            }
            logger.info("[{}] 自动注册完成，共注册 {} 个实例", config.getName(), registryMap.size());
        } catch (Exception e) {
            logger.error("[{}] 自动扫描注册失败", config.getName(), e);
        }
    }
    
    /**
     * 扫描包下所有的类
     */
    private List<Class<?>> scanClasses(String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
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
            logger.error("[{}] 扫描包失败: {}", config.getName(), path, e);
        }
        
        return classes;
    }
    
    /**
     * 在目录中查找所有的类
     */
    private List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
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
     * 从类定义注册实例
     */
    private void registerFromClass(Class<?> clazz) {
        try {
            // 检查是否有指定的注解
            if (!clazz.isAnnotationPresent(config.getAnnotationClass())) {
                return; // 没有注解，不注册
            }
            
            // 使用实例化函数创建实例
            T instance = config.getInstanceFactory().apply(clazz);
            
            // 使用键提取函数获取注册键
            String key = config.getKeyExtractor().apply(clazz, instance);
            
            // 注册
            registryMap.put(key, instance);
            logger.debug("[{}] 自动注册: {} -> {}", config.getName(), key, clazz.getSimpleName());
        } catch (Exception e) {
            logger.error("[{}] 注册实例失败: {}", config.getName(), clazz.getName(), e);
        }
    }
    
    /**
     * 根据键获取实例
     */
    public T get(String key) {
        return registryMap.get(key);
    }
    
    /**
     * 手动注册实例
     */
    public void register(String key, T instance) {
        registryMap.put(key, instance);
        logger.debug("[{}] 手动注册: {} -> {}", config.getName(), key, 
            instance != null ? instance.getClass().getSimpleName() : "null");
    }
    
    /**
     * 检查是否包含指定键的实例
     */
    public boolean has(String key) {
        return registryMap.containsKey(key);
    }
    
    /**
     * 获取所有已注册的键
     */
    public java.util.Set<String> getRegisteredKeys() {
        return registryMap.keySet();
    }
    
    /**
     * 清空所有注册的实例
     */
    public void clear() {
        registryMap.clear();
    }
    
    /**
     * 获取已注册的实例数量
     */
    public int size() {
        return registryMap.size();
    }
    
    /**
     * 扫描器配置类
     */
    public static class ScannerConfig<T> {
        private String name;
        private String packageName;
        private Class<?> targetType;
        private Class<? extends java.lang.annotation.Annotation> annotationClass;
        private Function<Class<?>, T> instanceFactory;
        private BiFunction<Class<?>, T, String> keyExtractor;
        private boolean autoScan = true;
        
        /**
         * 设置扫描器名称（用于日志）
         */
        public ScannerConfig<T> name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * 设置要扫描的包路径
         */
        public ScannerConfig<T> packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }
        
        /**
         * 设置目标类型（必须是该类型的子类才会被注册）
         */
        public ScannerConfig<T> targetType(Class<?> targetType) {
            this.targetType = targetType;
            return this;
        }
        
        /**
         * 设置触发注册的注解类
         */
        public ScannerConfig<T> annotationClass(Class<? extends java.lang.annotation.Annotation> annotationClass) {
            this.annotationClass = annotationClass;
            return this;
        }
        
        /**
         * 设置实例化工厂函数
         * 
         * @param factory 接收Class对象，返回实例的函数
         */
        public ScannerConfig<T> instanceFactory(Function<Class<?>, T> factory) {
            this.instanceFactory = factory;
            return this;
        }
        
        /**
         * 设置键提取函数
         * 
         * @param extractor 接收Class对象和实例，返回注册键的函数
         */
        public ScannerConfig<T> keyExtractor(BiFunction<Class<?>, T, String> extractor) {
            this.keyExtractor = extractor;
            return this;
        }
        
        /**
         * 设置是否自动扫描（默认true）
         */
        public ScannerConfig<T> autoScan(boolean autoScan) {
            this.autoScan = autoScan;
            return this;
        }
        
        /**
         * 构建配置
         */
        public AnnotationScannerRegistry<T> build() {
            if (packageName == null || packageName.isEmpty()) {
                throw new IllegalArgumentException("包路径不能为空");
            }
            if (targetType == null) {
                throw new IllegalArgumentException("目标类型不能为空");
            }
            if (annotationClass == null) {
                throw new IllegalArgumentException("注解类不能为空");
            }
            if (instanceFactory == null) {
                // 默认使用无参构造函数
                this.instanceFactory = clazz -> {
                    try {
                        return (T) clazz.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("实例化失败: " + clazz.getName(), e);
                    }
                };
            }
            if (keyExtractor == null) {
                throw new IllegalArgumentException("键提取函数不能为空");
            }
            if (name == null || name.isEmpty()) {
                this.name = "Registry";
            }
            
            return new AnnotationScannerRegistry<>(this);
        }
        
        // Getters
        public String getName() { return name; }
        public String getPackageName() { return packageName; }
        public Class<?> getTargetType() { return targetType; }
        public Class<? extends java.lang.annotation.Annotation> getAnnotationClass() { return annotationClass; }
        public Function<Class<?>, T> getInstanceFactory() { return instanceFactory; }
        public BiFunction<Class<?>, T, String> getKeyExtractor() { return keyExtractor; }
        public boolean isAutoScan() { return autoScan; }
    }
    
    /**
     * 双参数函数接口
     */
    @FunctionalInterface
    public interface BiFunction<T, U, R> {
        R apply(T t, U u);
    }
}

# AnnotationScannerRegistry 使用示例

## 简介

`AnnotationScannerRegistry` 是一个通用的注解扫描注册工具类，可以自动扫描指定包下带有特定注解的类，并注册到映射表中。

## 基本用法

### 1. 定义注解

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAnnotation {
    String value() default "";
}
```

### 2. 定义接口或基类

```java
public interface MyService {
    String getName();
    void execute();
}
```

### 3. 实现类并添加注解

```java
@MyAnnotation("serviceA")
public class ServiceA implements MyService {
    @Override
    public String getName() {
        return "Service A";
    }
    
    @Override
    public void execute() {
        System.out.println("Executing Service A");
    }
}

@MyAnnotation("serviceB")
public class ServiceB implements MyService {
    @Override
    public String getName() {
        return "Service B";
    }
    
    @Override
    public void execute() {
        System.out.println("Executing Service B");
    }
}
```

### 4. 创建注册器

```java
public class MyServiceRegistry {
    
    private static final String PACKAGE_NAME = "com.example.myservice";
    
    private static final AnnotationScannerRegistry<MyService> registry;
    
    static {
        registry = new AnnotationScannerRegistry.ScannerConfig<MyService>()
            .name("MyServiceRegistry")  // 注册器名称（用于日志）
            .packageName(PACKAGE_NAME)  // 要扫描的包路径
            .targetType(MyService.class)  // 目标类型
            .annotationClass(MyAnnotation.class)  // 触发注册的注解
            .instanceFactory(clazz -> {
                // 实例化工厂函数
                try {
                    return (MyService) clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("实例化失败: " + clazz.getName(), e);
                }
            })
            .keyExtractor((clazz, instance) -> {
                // 键提取函数 - 从注解或实例中提取注册键
                MyAnnotation annotation = clazz.getAnnotation(MyAnnotation.class);
                return annotation.value().isEmpty() ? instance.getName() : annotation.value();
            })
            .build();
    }
    
    // 获取服务
    public static MyService getService(String name) {
        return registry.get(name);
    }
    
    // 手动注册
    public static void registerService(String name, MyService service) {
        registry.register(name, service);
    }
    
    // 检查是否存在
    public static boolean hasService(String name) {
        return registry.has(name);
    }
    
    // 获取所有已注册的名称
    public static Set<String> getRegisteredNames() {
        return registry.getRegisteredKeys();
    }
    
    // 清空所有注册
    public static void clear() {
        registry.clear();
    }
}
```

## 配置说明

### ScannerConfig 配置项

| 配置项 | 说明 | 是否必需 | 默认值 |
|--------|------|----------|--------|
| name | 注册器名称，用于日志输出 | 否 | "Registry" |
| packageName | 要扫描的包路径 | 是 | 无 |
| targetType | 目标类型，只有该类型的子类才会被注册 | 是 | 无 |
| annotationClass | 触发注册的注解类 | 是 | 无 |
| instanceFactory | 实例化工厂函数 | 否 | 使用无参构造函数 |
| keyExtractor | 键提取函数 | 是 | 无 |
| autoScan | 是否自动扫描 | 否 | true |

## 高级用法

### 1. 自定义实例化逻辑

```java
.instanceFactory(clazz -> {
    // 可以从Spring容器中获取
    return applicationContext.getBean(clazz);
})
```

### 2. 复杂的键提取逻辑

```java
.keyExtractor((clazz, instance) -> {
    MyAnnotation annotation = clazz.getAnnotation(MyAnnotation.class);
    // 优先使用注解值，否则使用实例的方法
    return annotation.value().isEmpty() ? instance.getName() : annotation.value();
})
```

### 3. 延迟扫描

```java
// 创建时不自动扫描
AnnotationScannerRegistry<MyService> registry = new AnnotationScannerRegistry.ScannerConfig<MyService>()
    .name("MyServiceRegistry")
    .packageName(PACKAGE_NAME)
    .targetType(MyService.class)
    .annotationClass(MyAnnotation.class)
    .keyExtractor((clazz, instance) -> instance.getName())
    .autoScan(false)  // 禁用自动扫描
    .build();

// 在需要时手动触发扫描
registry.autoScanAndRegister();  // 注意：需要将方法改为public
```

### 4. 手动注册和自动扫描混合使用

```java
// 自动扫描注册的类
// ...

// 手动注册额外的实例
registry.register("customService", new CustomService());
```

## 实际应用场景

1. **插件系统**：自动扫描并注册插件
2. **策略模式**：自动注册不同的策略实现
3. **处理器注册**：自动注册不同类型的事件处理器
4. **转换器注册**：如 ConverterRegistry 的实现
5. **服务发现**：自动发现并注册服务实现

## 注意事项

1. 包路径必须是完整的包名（如 `com.example.service`）
2. 类必须有无参构造函数（除非自定义实例化逻辑）
3. 注解必须设置 `@Retention(RetentionPolicy.RUNTIME)` 才能在运行时获取
4. 扫描只在类加载时执行一次，动态添加的类不会被自动扫描到
5. 线程安全：内部使用 `ConcurrentHashMap` 保证线程安全

package com.ruleframe.core.operator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动注册运算符的注解
 * 使用此注解标记的运算符类会被 OperatorRegistry 自动扫描并注册
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoRegisterOperator {
    /**
     * 运算符名称，用于注册和获取
     * 例如: ">", ">=", "<", "<=", "="
     */
    String value();
}

package com.ruleframe.core;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * 全局编码初始化器
 * 在应用启动时设置控制台输出编码为 UTF-8
 * 只需在 main 方法开头调用一次即可
 */
public class EncodingInitializer {

    /**
     * 初始化全局编码设置
     * 必须在 main 方法的第一行调用
     */
    public static void init() {
        try {
            // 设置系统属性
            System.setProperty("file.encoding", "UTF-8");
            System.setProperty("sun.stdout.encoding", "UTF-8");
            System.setProperty("sun.stderr.encoding", "UTF-8");

            // 重新设置 System.out 和 System.err 为 UTF-8 编码
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
            System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
        } catch (Exception e) {
            // 忽略初始化异常
        }
    }
}

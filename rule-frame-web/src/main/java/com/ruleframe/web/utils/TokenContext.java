package com.ruleframe.web.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Token上下文工具类
 * 用于在当前请求上下文中获取用户信息
 */
public class TokenContext {

    /**
     * 获取当前请求对象
     */
    private static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("当前不在HTTP请求上下文中");
        }
        return attributes.getRequest();
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        HttpServletRequest request = getCurrentRequest();
        Object userId = request.getAttribute("userId");
        if (userId == null) {
            throw new IllegalStateException("未找到当前用户ID，请先登录");
        }
        return Long.valueOf(userId.toString());
    }

    /**
     * 获取当前用户名
     *
     * @return 用户名
     */
    public static String getCurrentUsername() {
        HttpServletRequest request = getCurrentRequest();
        Object username = request.getAttribute("username");
        if (username == null) {
            throw new IllegalStateException("未找到当前用户名，请先登录");
        }
        return username.toString();
    }

    /**
     * 获取当前用户角色
     *
     * @return 用户角色
     */
    public static String getCurrentRole() {
        HttpServletRequest request = getCurrentRequest();
        Object role = request.getAttribute("role");
        if (role == null) {
            throw new IllegalStateException("未找到当前用户角色，请先登录");
        }
        return role.toString();
    }

    /**
     * 获取当前用户的Token
     *
     * @return JWT Token
     */
    public static String getCurrentToken() {
        HttpServletRequest request = getCurrentRequest();
        Object token = request.getAttribute("token");
        if (token == null) {
            throw new IllegalStateException("未找到当前用户Token，请先登录");
        }
        return token.toString();
    }

    /**
     * 检查当前用户是否已登录
     *
     * @return 是否已登录
     */
    public static boolean isAuthenticated() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return false;
            }
            HttpServletRequest request = attributes.getRequest();
            return request.getAttribute("userId") != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查当前用户是否是管理员
     *
     * @return 是否是管理员
     */
    public static boolean isAdmin() {
        try {
            String role = getCurrentRole();
            return "admin".equals(role);
        } catch (Exception e) {
            return false;
        }
    }
}

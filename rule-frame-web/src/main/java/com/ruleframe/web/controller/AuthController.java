package com.ruleframe.web.controller;

import com.ruleframe.web.dto.*;
import com.ruleframe.web.entity.Menu;
import com.ruleframe.web.service.MenuService;
import com.ruleframe.web.service.UserService;
import com.ruleframe.web.utils.JwtUtil;
import com.ruleframe.web.utils.TokenContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final MenuService menuService;
    private final JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            log.info("用户登录请求: username={}", request.getUsername());
            LoginResponse response = userService.login(request);
            if (response == null) {
                return ApiResponse.error(401, "用户名或密码错误");
            }
            
            log.info("登录成功，用户信息: id={}, role={}", response.getUser().getId(), response.getUser().getRole());
            log.info("查询到菜单数量: {}", response.getMenus() != null ? response.getMenus().size() : 0);
            
            return ApiResponse.success(response);
        } catch (RuntimeException e) {
            log.error("登录失败: {}", e.getMessage(), e);
            return ApiResponse.error(401, e.getMessage());
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        // 实际项目中需要清除token等操作
        return ApiResponse.success();
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/profile")
    public ApiResponse<UserDTO> getProfile() {
        try {
            // 从Token上下文中获取当前用户ID
            Long userId = TokenContext.getCurrentUserId();
            log.info("获取用户信息: userId={}", userId);
            
            UserDTO user = userService.getUserById(userId);
            if (user == null) {
                return ApiResponse.error(404, "用户不存在");
            }
            return ApiResponse.success(user);
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage(), e);
            return ApiResponse.error(500, "获取用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    public ApiResponse<String> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            // 去除Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            String newToken = jwtUtil.refreshToken(token);
            log.info("Token刷新成功");
            
            return ApiResponse.success(newToken);
        } catch (Exception e) {
            log.error("Token刷新失败: {}", e.getMessage(), e);
            return ApiResponse.error(401, "Token刷新失败: " + e.getMessage());
        }
    }
}

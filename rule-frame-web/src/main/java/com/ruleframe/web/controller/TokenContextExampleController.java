package com.ruleframe.web.controller;

import com.ruleframe.web.dto.ApiResponse;
import com.ruleframe.web.dto.UserDTO;
import com.ruleframe.web.service.UserService;
import com.ruleframe.web.utils.TokenContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * TokenContext使用示例控制器
 * 展示如何在业务代码中使用TokenContext获取当前用户信息
 */
@Slf4j
@RestController
@RequestMapping("/api/example")
@RequiredArgsConstructor
public class TokenContextExampleController {

    private final UserService userService;

    /**
     * 示例1：获取当前用户ID
     */
    @GetMapping("/current-user-id")
    public ApiResponse<Long> getCurrentUserId() {
        // 从Token上下文中获取当前用户ID
        Long userId = TokenContext.getCurrentUserId();
        log.info("当前用户ID: {}", userId);
        return ApiResponse.success(userId);
    }

    /**
     * 示例2：获取当前用户名
     */
    @GetMapping("/current-username")
    public ApiResponse<String> getCurrentUsername() {
        // 从Token上下文中获取当前用户名
        String username = TokenContext.getCurrentUsername();
        log.info("当前用户名: {}", username);
        return ApiResponse.success(username);
    }

    /**
     * 示例3：获取当前用户角色
     */
    @GetMapping("/current-role")
    public ApiResponse<String> getCurrentRole() {
        // 从Token上下文中获取当前用户角色
        String role = TokenContext.getCurrentRole();
        log.info("当前用户角色: {}", role);
        return ApiResponse.success(role);
    }

    /**
     * 示例4：检查是否是管理员
     */
    @GetMapping("/is-admin")
    public ApiResponse<Boolean> isAdmin() {
        // 检查当前用户是否是管理员
        boolean isAdmin = TokenContext.isAdmin();
        log.info("是否是管理员: {}", isAdmin);
        return ApiResponse.success(isAdmin);
    }

    /**
     * 示例5：在业务逻辑中使用TokenContext
     * 比如：查询当前用户的详细信息
     */
    @GetMapping("/my-profile")
    public ApiResponse<UserDTO> getMyProfile() {
        // 从Token上下文中获取当前用户ID
        Long userId = TokenContext.getCurrentUserId();
        
        // 根据用户ID查询用户信息
        UserDTO user = userService.getUserById(userId);
        
        log.info("查询当前用户信息: userId={}, username={}", userId, user.getUsername());
        return ApiResponse.success(user);
    }

    /**
     * 示例6：根据用户角色进行权限控制
     */
    @GetMapping("/admin-only")
    public ApiResponse<String> adminOnly() {
        // 检查是否是管理员
        if (!TokenContext.isAdmin()) {
            return ApiResponse.error(403, "只有管理员才能访问此接口");
        }
        
        // 管理员专属逻辑
        return ApiResponse.success("欢迎管理员！");
    }

    /**
     * 示例7：获取当前Token
     */
    @GetMapping("/current-token")
    public ApiResponse<String> getCurrentToken() {
        // 获取当前的JWT Token
        String token = TokenContext.getCurrentToken();
        log.info("当前Token: {}", token);
        return ApiResponse.success(token);
    }
}

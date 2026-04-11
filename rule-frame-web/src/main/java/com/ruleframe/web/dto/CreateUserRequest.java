package com.ruleframe.web.dto;

import lombok.Data;

/**
 * 创建用户请求
 */
@Data
public class CreateUserRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 角色：admin-管理员，user-普通用户
     */
    private String role;
}

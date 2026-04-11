package com.ruleframe.web.dto;

import lombok.Data;

/**
 * 更新用户请求
 */
@Data
public class UpdateUserRequest {

    /**
     * 用户ID
     */
    private Long id;

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

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}

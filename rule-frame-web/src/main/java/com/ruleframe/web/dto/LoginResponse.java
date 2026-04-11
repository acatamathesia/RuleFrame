package com.ruleframe.web.dto;

import com.ruleframe.web.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * 登录响应
 */
@Data
public class LoginResponse {

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 用户信息
     */
    private UserDTO user;

    /**
     * 用户菜单列表
     */
    private List<Menu> menus;
}

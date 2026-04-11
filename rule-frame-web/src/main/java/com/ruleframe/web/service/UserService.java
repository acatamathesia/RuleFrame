package com.ruleframe.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruleframe.web.dto.*;
import com.ruleframe.web.entity.Menu;
import com.ruleframe.web.entity.User;
import com.ruleframe.web.mapper.UserMapper;
import com.ruleframe.web.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final MenuService menuService;

    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest request) {
        // 从数据库查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, request.getUsername());
        
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            return null;
        }

        // 验证密码
        if (!user.getPassword().equals(request.getPassword())) {
            return null;
        }

        if (user.getStatus() == 0) {
            throw new RuntimeException("用户已被禁用");
        }

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        // 生成JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        // 获取用户菜单
        List<Menu> menus = menuService.getMenuTreeByUserId(user.getId());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUser(convertToDTO(user));
        response.setMenus(menus);
        return response;
    }

    /**
     * 根据ID获取用户
     */
    public UserDTO getUserById(Long id) {
        User user = userMapper.selectById(id);
        return user != null ? convertToDTO(user) : null;
    }

    /**
     * 根据用户名获取用户
     */
    public UserDTO getUserByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        
        User user = userMapper.selectOne(queryWrapper);
        return user != null ? convertToDTO(user) : null;
    }

    /**
     * 分页查询用户
     */
    public PageResult<UserDTO> listUsers(String keyword, Integer status, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(User::getUsername, keyword)
                       .or()
                       .like(User::getNickname, keyword);
        }
        
        // 状态过滤
        if (status != null) {
            queryWrapper.eq(User::getStatus, status);
        }
        
        // 排序
        queryWrapper.orderByDesc(User::getCreateTime);
        
        // 分页查询
        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> userPage = userMapper.selectPage(page, queryWrapper);
        
        // 转换为DTO
        List<UserDTO> list = userPage.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return PageResult.of(list, userPage.getTotal(), pageNum, pageSize);
    }

    /**
     * 创建用户
     */
    public UserDTO createUser(CreateUserRequest request) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, request.getUsername());
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole() != null ? request.getRole() : "user");
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);
        return convertToDTO(user);
    }

    /**
     * 更新用户
     */
    public UserDTO updateUser(UpdateUserRequest request) {
        User user = userMapper.selectById(request.getId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        user.setUpdateTime(LocalDateTime.now());

        userMapper.updateById(user);
        return convertToDTO(user);
    }

    /**
     * 删除用户
     */
    public void deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if ("admin".equals(user.getUsername())) {
            throw new RuntimeException("不能删除管理员账号");
        }
        // 使用MyBatis-Plus的逻辑删除
        userMapper.deleteById(id);
    }

    /**
     * 修改用户状态
     */
    public UserDTO toggleUserStatus(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if ("admin".equals(user.getUsername())) {
            throw new RuntimeException("不能禁用管理员账号");
        }
        user.setStatus(user.getStatus() == 1 ? 0 : 1);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        return convertToDTO(user);
    }

    /**
     * 修改密码
     */
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("原密码错误");
        }
        user.setPassword(newPassword);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
    }

    /**
     * 更新个人信息
     */
    public UserDTO updateProfile(Long userId, String nickname, String email, String phone) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        return convertToDTO(user);
    }

    /**
     * 获取用户总数
     */
    public long countUsers() {
        return userMapper.selectCount(null);
    }

    /**
     * 转换为DTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setNickname(user.getNickname());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAvatar(user.getAvatar());
        dto.setStatus(user.getStatus());
        dto.setRole(user.getRole());
        dto.setCreateTime(user.getCreateTime());
        dto.setLastLoginTime(user.getLastLoginTime());
        return dto;
    }
}

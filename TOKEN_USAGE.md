# Token认证系统使用说明

## 概述

系统已实现完整的JWT Token认证机制，包括：
- ✅ JWT Token生成和验证
- ✅ Token认证拦截器
- ✅ 从Token中获取用户信息的工具类
- ✅ 自动身份验证

## 核心组件

### 1. JwtUtil - JWT工具类
**位置**: `com.ruleframe.web.utils.JwtUtil`

**主要功能**:
- 生成JWT Token
- 解析Token获取用户信息
- 验证Token有效性
- 刷新Token

**主要方法**:
```java
// 生成Token
String token = jwtUtil.generateToken(userId, username, role);

// 从Token获取用户ID
Long userId = jwtUtil.getUserIdFromToken(token);

// 从Token获取用户名
String username = jwtUtil.getUsernameFromToken(token);

// 从Token获取用户角色
String role = jwtUtil.getRoleFromToken(token);

// 验证Token
boolean isValid = jwtUtil.validateToken(token);

// 刷新Token
String newToken = jwtUtil.refreshToken(oldToken);
```

### 2. TokenContext - Token上下文工具类 ⭐
**位置**: `com.ruleframe.web.utils.TokenContext`

**主要功能**: 在业务代码中方便地获取当前登录用户信息

**主要方法**:
```java
// 获取当前用户ID
Long userId = TokenContext.getCurrentUserId();

// 获取当前用户名
String username = TokenContext.getCurrentUsername();

// 获取当前用户角色
String role = TokenContext.getCurrentRole();

// 获取当前Token
String token = TokenContext.getCurrentToken();

// 检查是否已登录
boolean isLogin = TokenContext.isAuthenticated();

// 检查是否是管理员
boolean isAdmin = TokenContext.isAdmin();
```

### 3. TokenInterceptor - Token认证拦截器
**位置**: `com.ruleframe.web.interceptor.TokenInterceptor`

**功能**: 自动拦截所有API请求（除登录等公开接口外），验证Token有效性

### 4. WebMvcConfig - Web配置类
**位置**: `com.ruleframe.web.config.WebMvcConfig`

**功能**: 注册拦截器，配置不需要认证的路径

## 使用示例

### 示例1: 在Controller中获取当前用户信息

```java
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/my-profile")
    public ApiResponse<UserDTO> getMyProfile() {
        // 从Token上下文获取当前用户ID
        Long userId = TokenContext.getCurrentUserId();
        
        // 查询用户信息
        UserDTO user = userService.getUserById(userId);
        
        return ApiResponse.success(user);
    }
}
```

### 示例2: 根据角色进行权限控制

```java
@PostMapping("/admin/action")
public ApiResponse<String> adminAction() {
    // 检查是否是管理员
    if (!TokenContext.isAdmin()) {
        return ApiResponse.error(403, "权限不足");
    }
    
    // 管理员专属逻辑
    return ApiResponse.success("操作成功");
}
```

### 示例3: 在Service层获取当前用户

```java
@Service
@RequiredArgsConstructor
public class OrderService {
    
    public Order createOrder(OrderRequest request) {
        // 获取当前用户ID，作为订单创建人
        Long userId = TokenContext.getCurrentUserId();
        String username = TokenContext.getCurrentUsername();
        
        Order order = new Order();
        order.setCreatedBy(userId);
        order.setCreatedByName(username);
        // ... 其他逻辑
        
        return order;
    }
}
```

## 配置说明

### application.yml配置

```yaml
# JWT配置
jwt:
  secret: YourSecretKeyHere  # JWT密钥（生产环境请使用复杂的密钥）
  expiration: 86400000  # Token过期时间：24小时（毫秒）
```

### 排除认证的路径

在 `WebMvcConfig` 中配置不需要Token认证的路径：

```java
.excludePathPatterns(
    "/api/auth/login",      // 登录接口
    "/api/auth/register",   // 注册接口
    "/api/public/**",       // 公开接口
    "/api/doc/**",          // API文档
    "/swagger-ui/**",       // Swagger UI
    "/v3/api-docs/**"       // API文档
);
```

## 前端使用说明

### 1. 登录成功后保存Token

```typescript
const res = await loginApi({ username, password });
localStorage.setItem('token', res.data.token);
```

### 2. 请求自动携带Token

前端已配置请求拦截器，会自动在请求头添加Token：

```typescript
// request.ts中已配置
request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

### 3. Token过期处理

响应拦截器会自动处理401错误：

```typescript
request.interceptors.response.use(
  (response) => { /* ... */ },
  (error) => {
    if (error.response?.status === 401) {
      // Token过期或无效，跳转到登录页
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
  }
);
```

## API接口

### 1. 用户登录
```
POST /api/auth/login
Request: { "username": "admin", "password": "123456" }
Response: {
  "code": 200,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": { "id": 1, "username": "admin", ... },
    "menus": [...]
  }
}
```

### 2. 获取当前用户信息
```
GET /api/auth/profile
Header: Authorization: Bearer {token}
Response: {
  "code": 200,
  "data": { "id": 1, "username": "admin", ... }
}
```

### 3. 刷新Token
```
POST /api/auth/refresh
Header: Authorization: Bearer {token}
Response: {
  "code": 200,
  "data": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## 测试示例接口

系统提供了示例Controller用于测试TokenContext功能：

```
GET /api/example/current-user-id      # 获取当前用户ID
GET /api/example/current-username     # 获取当前用户名
GET /api/example/current-role         # 获取当前用户角色
GET /api/example/is-admin             # 检查是否是管理员
GET /api/example/my-profile           # 获取当前用户详细信息
GET /api/example/admin-only           # 管理员专属接口
GET /api/example/current-token        # 获取当前Token
```

## 注意事项

1. **密钥安全**: 生产环境必须修改JWT密钥，使用复杂且安全的密钥
2. **Token过期**: 建议设置合理的过期时间（如24小时），并提供刷新机制
3. **HTTPS**: 生产环境建议使用HTTPS传输，防止Token被窃取
4. **权限控制**: 敏感操作除了检查Token外，还应进行权限验证
5. **异常处理**: TokenContext在未登录状态下会抛出异常，注意捕获处理

## 安全建议

1. 定期更换JWT密钥
2. 实现Token黑名单机制（可选）
3. 记录登录日志和异常日志
4. 实现密码加密（如BCrypt）
5. 添加验证码防止暴力破解
6. 实现登录失败次数限制

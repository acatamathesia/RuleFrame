# 接口测试说明文档

## 概述

本项目已为所有后端接口创建了完整的单元测试，使用Spring Boot Test + MockMvc进行测试。

## 测试类列表

### 1. AuthControllerTest - 认证接口测试
**文件**: `AuthControllerTest.java`  
**测试接口**:
- ✅ POST `/api/auth/login` - 用户登录
- ✅ POST `/api/auth/logout` - 用户登出
- ✅ GET `/api/auth/profile` - 获取当前用户信息
- ✅ POST `/api/auth/refresh` - 刷新Token

**测试场景**:
- 登录成功
- 登录失败（密码错误）
- 登录失败（用户不存在）
- 获取用户信息（带Token）
- 获取用户信息（无Token）
- 刷新Token
- Token无效

**测试数量**: 8个

---

### 2. UserControllerTest - 用户管理接口测试
**文件**: `UserControllerTest.java`  
**测试接口**:
- ✅ GET `/api/users` - 分页查询用户列表
- ✅ GET `/api/users/{id}` - 根据ID获取用户
- ✅ POST `/api/users` - 创建用户
- ✅ PUT `/api/users/{id}` - 更新用户
- ✅ DELETE `/api/users/{id}` - 删除用户
- ✅ PUT `/api/users/{id}/toggle-status` - 切换用户状态
- ✅ PUT `/api/users/{id}/password` - 修改密码
- ✅ PUT `/api/users/{id}/profile` - 更新个人信息

**测试场景**:
- 分页查询（正常、带关键词搜索）
- 创建用户（成功、重复用户名）
- 查询用户（存在、不存在）
- 更新用户信息
- 切换用户状态
- 修改密码
- 更新个人信息
- 删除用户
- 未授权访问

**测试数量**: 12个

---

### 3. MenuControllerTest - 菜单管理接口测试
**文件**: `MenuControllerTest.java`  
**测试接口**:
- ✅ GET `/api/menus/tree` - 查询菜单树
- ✅ GET `/api/menus/all` - 查询所有菜单
- ✅ GET `/api/menus/{id}` - 根据ID查询菜单
- ✅ POST `/api/menus` - 创建菜单
- ✅ PUT `/api/menus/{id}` - 更新菜单
- ✅ DELETE `/api/menus/{id}` - 删除菜单
- ✅ DELETE `/api/menus/batch` - 批量删除菜单
- ✅ PUT `/api/menus/{id}/status` - 更新菜单状态
- ✅ GET `/api/menus/user/{userId}` - 根据用户ID查询菜单树

**测试场景**:
- 查询菜单树
- 查询所有菜单
- 创建菜单
- 查询菜单（存在、不存在）
- 更新菜单
- 更新菜单状态
- 根据用户ID查询菜单
- 批量删除菜单
- 删除菜单
- 未授权访问

**测试数量**: 11个

---

### 4. RoleControllerTest - 角色管理接口测试
**文件**: `RoleControllerTest.java`  
**测试接口**:
- ✅ GET `/api/roles/page` - 分页查询角色列表
- ✅ GET `/api/roles/all` - 查询所有角色
- ✅ GET `/api/roles/{id}` - 根据ID查询角色
- ✅ POST `/api/roles` - 创建角色
- ✅ PUT `/api/roles/{id}` - 更新角色
- ✅ DELETE `/api/roles/{id}` - 删除角色
- ✅ DELETE `/api/roles/batch` - 批量删除角色
- ✅ PUT `/api/roles/{id}/status` - 更新角色状态
- ✅ POST `/api/roles/{roleId}/menus` - 为角色分配菜单
- ✅ GET `/api/roles/{roleId}/menus` - 查询角色的菜单ID列表

**测试场景**:
- 分页查询（正常、带角色名称搜索）
- 查询所有角色
- 创建角色
- 查询角色（存在、不存在）
- 更新角色
- 更新角色状态
- 为角色分配菜单
- 查询角色菜单
- 批量删除角色
- 删除角色
- 未授权访问

**测试数量**: 13个

---

### 5. SystemControllerTest - 系统监控接口测试
**文件**: `SystemControllerTest.java`  
**测试接口**:
- ✅ GET `/api/system/info` - 获取系统信息
- ✅ GET `/api/system/dashboard` - 获取仪表盘统计数据
- ✅ GET `/api/system/start-time` - 获取应用启动时间
- ✅ GET `/api/system/health` - 健康检查

**测试场景**:
- 获取系统信息（验证各项字段）
- 获取仪表盘统计
- 获取启动时间
- 健康检查（带Token、不带Token）
- 验证内存信息合理性
- 验证Java版本
- 验证用户数
- 未授权访问

**测试数量**: 10个

---

### 6. RuleControllerTest - 规则引擎接口测试
**文件**: `RuleControllerTest.java`  
**测试接口**:
- ✅ POST `/api/rules/execute` - 执行单个规则
- ✅ POST `/api/rules/context/execute` - 执行规则上下文
- ✅ GET `/api/rules/health` - 规则引擎健康检查

**测试场景**:
- 执行单个规则
- 执行规则上下文（多个规则）
- 规则引擎健康检查
- 空规则名
- 空规则列表
- 特殊字符规则名
- 中文规则名
- 单个规则上下文
- 大量规则（20个）
- 未授权访问

**测试数量**: 11个

---

## 测试总计

| 测试类 | 测试数量 | 状态 |
|--------|---------|------|
| AuthControllerTest | 8 | ✅ |
| UserControllerTest | 12 | ✅ |
| MenuControllerTest | 11 | ✅ |
| RoleControllerTest | 13 | ✅ |
| SystemControllerTest | 10 | ✅ |
| RuleControllerTest | 11 | ✅ |
| **总计** | **65** | **✅** |

---

## 如何运行测试

### 方式1: 运行所有测试

```bash
# 在项目根目录执行
cd RuleFrame
mvn test
```

### 方式2: 运行单个测试类

```bash
# 运行认证测试
mvn test -Dtest=AuthControllerTest

# 运行用户管理测试
mvn test -Dtest=UserControllerTest

# 运行菜单管理测试
mvn test -Dtest=MenuControllerTest

# 运行角色管理测试
mvn test -Dtest=RoleControllerTest

# 运行系统监控测试
mvn test -Dtest=SystemControllerTest

# 运行规则引擎测试
mvn test -Dtest=RuleControllerTest
```

### 方式3: 运行单个测试方法

```bash
# 运行登录测试
mvn test -Dtest=AuthControllerTest#testLoginSuccess

# 运行创建用户测试
mvn test -Dtest=UserControllerTest#testCreateUser
```

### 方式4: 使用IDE运行

在IDEA或Eclipse中：
1. 右键点击测试类或测试方法
2. 选择 "Run 'xxxTest'" 或 "Debug 'xxxTest'"

### 方式5: 使用PowerShell运行（Windows）

```powershell
# 进入项目目录
cd d:\project\RuleFrameProject\RuleFrame

# 运行所有测试
mvn test

# 运行指定测试类
mvn test -Dtest=AuthControllerTest,UserControllerTest
```

---

## 测试前置条件

### 1. 数据库准备

确保MySQL数据库已启动，并且已执行初始化脚本：

```bash
# 数据库连接信息
Host: localhost:3306
Database: ruleframe
Username: root
Password: 123456
```

### 2. 测试数据

测试会自动创建和清理测试数据，但需要确保数据库中存在admin用户：

```sql
-- 如果admin用户不存在，请先执行
INSERT INTO sys_user (username, password, nickname, email, phone, role, status) 
VALUES ('admin', 'admin123', '管理员', 'admin@ruleframe.com', '13800138000', 'admin', 1);
```

### 3. 应用启动

测试会自动启动Spring Boot应用，无需手动启动。

---

## 测试特性

### 1. 自动Token管理

所有需要认证的测试都会自动登录获取Token：

```java
@BeforeEach
void setUp() throws Exception {
    // 自动登录获取Token
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("admin");
    loginRequest.setPassword("admin123");
    
    MvcResult result = mockMvc.perform(post("/api/auth/login")...)
    token = response.getData().getToken();
}
```

### 2. 测试顺序控制

使用`@TestMethodOrder`和`@Order`注解控制测试执行顺序：

```java
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest {
    
    @Test
    @Order(1)
    void testLoginSuccess() { ... }
    
    @Test
    @Order(2)
    void testGetProfile() { ... }
}
```

### 3. 测试数据隔离

每个测试类独立管理自己的测试数据：
- 创建的数据会在测试结束时删除
- 使用静态变量保存创建的ID供后续测试使用

### 4. 详细的测试输出

测试会打印关键信息便于调试：

```java
System.out.println("✅ 登录成功，Token: " + token.substring(0, 30) + "...");
System.out.println("✅ 创建用户成功，用户ID: " + testUserId);
```

---

## 测试覆盖的接口

### 认证模块 (4个接口)
- [x] 用户登录
- [x] 用户登出
- [x] 获取用户信息
- [x] 刷新Token

### 用户管理 (8个接口)
- [x] 分页查询用户
- [x] 获取用户详情
- [x] 创建用户
- [x] 更新用户
- [x] 删除用户
- [x] 切换用户状态
- [x] 修改密码
- [x] 更新个人信息

### 菜单管理 (9个接口)
- [x] 查询菜单树
- [x] 查询所有菜单
- [x] 获取菜单详情
- [x] 创建菜单
- [x] 更新菜单
- [x] 删除菜单
- [x] 批量删除菜单
- [x] 更新菜单状态
- [x] 根据用户查询菜单

### 角色管理 (10个接口)
- [x] 分页查询角色
- [x] 查询所有角色
- [x] 获取角色详情
- [x] 创建角色
- [x] 更新角色
- [x] 删除角色
- [x] 批量删除角色
- [x] 更新角色状态
- [x] 分配菜单
- [x] 查询角色菜单

### 系统监控 (4个接口)
- [x] 获取系统信息
- [x] 获取仪表盘统计
- [x] 获取启动时间
- [x] 健康检查

### 规则引擎 (3个接口)
- [x] 执行规则
- [x] 执行规则上下文
- [x] 健康检查

**总计: 38个接口，65个测试用例**

---

## 常见问题

### Q1: 测试失败：数据库连接失败

**解决方案**:
1. 检查MySQL是否启动
2. 检查`application.yml`中的数据库配置
3. 确认数据库`ruleframe`已创建

### Q2: 测试失败：401 Unauthorized

**解决方案**:
1. 确认admin用户存在且密码为`admin123`
2. 检查JWT配置是否正确
3. 查看控制台日志确认Token生成成功

### Q3: 测试失败：用户已存在

**解决方案**:
1. 测试会自动清理数据，如果之前测试中断可能导致数据残留
2. 手动删除测试数据后重新运行

### Q4: 如何查看详细的测试报告？

**解决方案**:
```bash
# 测试报告位置
RuleFrame/rule-frame-web/target/surefire-reports/

# 查看HTML报告（如果有）
RuleFrame/rule-frame-web/target/site/surefire-report.html
```

### Q5: 测试运行很慢怎么办？

**解决方案**:
1. 每个测试类都会启动Spring容器，这是正常的
2. 可以单独运行某个测试类加快速度
3. 使用IDE的测试运行器可以选择性运行

---

## 测试最佳实践

1. **运行顺序**: 建议按顺序运行测试，因为某些测试依赖前面创建的数据
2. **数据清理**: 测试会在最后清理创建的测试数据
3. **隔离测试**: 如果需要单独运行某个测试，确保前置数据已准备好
4. **查看日志**: 测试失败时查看控制台输出和日志文件
5. **调试模式**: 使用IDE的Debug模式可以逐步排查问题

---

## 持续集成

可以将测试集成到CI/CD流程中：

```yaml
# GitHub Actions 示例
name: Run Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      - name: Run Tests
        run: mvn test
```

---

## 测试报告

运行测试后会生成测试报告，包括：
- 测试通过率
- 测试执行时间
- 失败用例详情
- 代码覆盖率（如果配置了）

---

## 更新日志

### 2024-XX-XX
- ✅ 创建AuthControllerTest（8个测试）
- ✅ 创建UserControllerTest（12个测试）
- ✅ 创建MenuControllerTest（11个测试）
- ✅ 创建RoleControllerTest（13个测试）
- ✅ 创建SystemControllerTest（10个测试）
- ✅ 创建RuleControllerTest（11个测试）
- ✅ 总计65个测试用例，覆盖38个接口

---

## 联系与支持

如有问题或建议，请反馈给开发团队。

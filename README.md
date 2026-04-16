# RuleFrame - 规则引擎框架

> 一个基于Java 21 + Spring Boot 3.4的轻量级规则引擎底座，提供灵活的规则配置、执行和管理能力。

## 📋 项目概述

RuleFrame 是一个企业级规则引擎框架，采用模块化设计，支持规则的配置化管理、动态执行和可视化维护。项目分为核心引擎层（rule-frame-core）和Web管理层（rule-frame-web），并配套Vue3前端管理界面。

### 核心特性

- ✅ **灵活的规则配置**：支持JSON文件、数据库等多种配置方式
- ✅ **强大的条件引擎**：支持叶子条件、组合条件、流式构建
- ✅ **多策略执行**：全匹配(ALL_MATCH)、首次失败(FIRST_FAIL)、首次成功(FIRST_SUCCESS)
- ✅ **类型转换系统**：自动注册转换器，支持多种数据类型
- ✅ **事实上下文**：支持Map、Bean、复合等多种事实上下文
- ✅ **路径解析**：支持嵌套数据的路径访问
- ✅ **权限管理**：完整的用户、角色、菜单权限体系
- ✅ **JWT认证**：安全的Token认证机制

## 🏗️ 技术栈

### 后端
- **Java**: 21
- **Spring Boot**: 3.4.0
- **MyBatis-Plus**: 3.5.5
- **MySQL**: 8.0.33
- **Jackson**: 2.18.2
- **Lombok**: 1.18.36
- **JWT**: Token认证

### 前端
- **Vue**: 3.x
- **TypeScript**: 5.x
- **Vite**: 5.x
- **Element Plus**: UI组件库
- **Pinia**: 状态管理
- **Vue Router**: 路由管理

## 📁 项目结构

```
RuleFrameProject/
├── RuleFrame/                          # 后端项目
│   ├── rule-frame-core/                # 核心规则引擎模块
│   │   └── src/main/java/com/ruleframe/
│   │       ├── config/                 # 配置加载与解析
│   │       │   ├── loader/             # 规则配置加载器
│   │       │   │   ├── CachedLoader.java          # 缓存加载器
│   │       │   │   ├── DatabaseLoader.java        # 数据库加载器
│   │       │   │   ├── JsonFileLoader.java        # JSON文件加载器
│   │       │   │   └── RuleConfigLoader.java      # 规则配置加载器接口
│   │       │   ├── parser/             # 规则配置解析器
│   │       │   │   ├── dto/            # 数据传输对象
│   │       │   │   │   ├── ConditionDto.java
│   │       │   │   │   ├── RuleDto.java
│   │       │   │   │   └── RuleGroupDto.java
│   │       │   │   ├── JsonFallternParser.java    # JSON回退解析器
│   │       │   │   └── RuleConfigParser.java      # 规则配置解析器
│   │       │   └── repository/         # 规则仓库
│   │       │       ├── ReloadableRepository.java  # 可重载仓库接口
│   │       │       └── RuleRepository.java        # 规则仓库接口
│   │       ├── core/                   # 核心引擎
│   │       │   ├── condition/          # 条件系统
│   │       │   │   ├── CompositeCondition.java    # 组合条件
│   │       │   │   ├── Condition.java             # 条件接口
│   │       │   │   ├── ConditionBuilder.java      # 条件构建器
│   │       │   │   ├── ConditionResult.java       # 条件结果
│   │       │   │   └── LeafCondition.java         # 叶子条件
│   │       │   ├── converter/          # 类型转换
│   │       │   │   ├── AutoRegisterConverter.java # 自动注册注解
│   │       │   │   ├── BigDecimalConverter.java   # 大数转换器
│   │       │   │   ├── ConverterRegistry.java     # 转换器注册表
│   │       │   │   └── ValueConverter.java        # 转换器接口
│   │       │   ├── element/            # 元素系统
│   │       │   │   ├── ConfigurableElement.java   # 可配置元素
│   │       │   │   ├── Element.java               # 元素接口
│   │       │   │   └── ElementValue.java          # 元素值
│   │       │   ├── fact/               # 事实上下文
│   │       │   │   ├── BeanFactContext.java       # Bean事实上下文
│   │       │   │   ├── CompositeFactContext.java  # 复合事实上下文
│   │       │   │   ├── FactContext.java           # 事实上下文接口
│   │       │   │   └── MapFactContext.java        # Map事实上下文
│   │       │   ├── group/              # 规则组
│   │       │   │   ├── strategy/       # 执行策略
│   │       │   │   │   ├── AllMatchStrategy.java      # 全匹配策略
│   │       │   │   │   ├── ExecutionStrategy.java     # 执行策略接口
│   │       │   │   │   ├── FirstFailStrategy.java     # 首次失败策略
│   │       │   │   │   └── FirstSuccessStrategy.java  # 首次成功策略
│   │       │   │   ├── GroupEvaluator.java        # 组评估器
│   │       │   │   ├── GroupResult.java           # 组执行结果
│   │       │   │   └── RuleGroup.java             # 规则组
│   │       │   ├── operator/           # 运算符
│   │       │   │   ├── number/         # 数字运算符
│   │       │   │   │   └── NumberOperator.java
│   │       │   │   ├── Operator.java              # 运算符接口
│   │       │   │   └── OperatorRegistry.java      # 运算符注册表
│   │       │   └── rule/               # 规则
│   │       │       ├── FailureAction.java         # 失败动作
│   │       │       ├── Rule.java                  # 规则
│   │       │       └── RuleResult.java            # 规则结果
│   │       ├── executor/               # 规则执行器
│   │       │   ├── context/            # 执行上下文
│   │       │   │   ├── ExecutionContext.java      # 执行上下文
│   │       │   │   └── ExecutionListener.java     # 执行监听器
│   │       │   ├── result/             # 执行结果
│   │       │   │   └── ExecutionReport.java       # 执行报告
│   │       │   └── RuleExecutor.java              # 规则执行器
│   │       ├── resolver/               # 路径解析
│   │       │   ├── MapPathResolver.java           # Map路径解析器
│   │       │   └── PathResolver.java              # 路径解析器接口
│   │       ├── exception/              # 异常
│   │       │   └── ConversionException.java       # 转换异常
│   │       ├── utils/                  # 工具类
│   │       │   ├── AnnotationScannerRegistry.java # 注解扫描注册器
│   │       │   └── JsonUtil.java                  # JSON工具
│   │       └── ApplicationContext.java              # 应用上下文
│   ├── rule-frame-web/                 # Web管理模块
│   │   └── src/main/java/com/ruleframe/web/
│   │       ├── config/                 # 配置类
│   │       │   ├── CorsConfig.java                # 跨域配置
│   │       │   ├── EncodingConfig.java            # 编码配置
│   │       │   ├── MybatisPlusConfig.java         # MyBatis-Plus配置
│   │       │   ├── RuleEngineConfig.java          # 规则引擎配置
│   │       │   └── WebMvcConfig.java              # Web MVC配置
│   │       ├── controller/             # 控制器
│   │       │   ├── AuthController.java            # 认证控制器
│   │       │   ├── MenuController.java            # 菜单控制器
│   │       │   ├── RoleController.java            # 角色控制器
│   │       │   ├── RuleController.java            # 规则控制器
│   │       │   ├── SystemController.java          # 系统控制器
│   │       │   ├── UserController.java            # 用户控制器
│   │       │   └── TokenContextExampleController.java
│   │       ├── dto/                    # 数据传输对象
│   │       │   ├── ApiResponse.java               # 统一响应
│   │       │   ├── CreateUserRequest.java
│   │       │   ├── DashboardStats.java
│   │       │   ├── LoginRequest.java
│   │       │   ├── LoginResponse.java
│   │       │   ├── PageResult.java
│   │       │   ├── SystemInfo.java
│   │       │   └── UpdateUserRequest.java
│   │       ├── entity/                 # 实体类
│   │       │   ├── Menu.java                      # 菜单实体
│   │       │   ├── Role.java                      # 角色实体
│   │       │   ├── RoleMenu.java                  # 角色菜单关联
│   │       │   ├── User.java                      # 用户实体
│   │       │   └── UserRole.java                  # 用户角色关联
│   │       ├── interceptor/            # 拦截器
│   │       │   └── TokenInterceptor.java          # Token拦截器
│   │       ├── mapper/                 # 数据访问层
│   │       │   ├── MenuMapper.java
│   │       │   ├── RoleMapper.java
│   │       │   ├── RoleMenuMapper.java
│   │       │   ├── UserMapper.java
│   │       │   └── UserRoleMapper.java
│   │       ├── service/                # 业务逻辑层
│   │       │   ├── MenuService.java
│   │       │   ├── RoleService.java
│   │       │   ├── UserService.java
│   │       │   └── impl/
│   │       │       ├── MenuServiceImpl.java
│   │       │       └── RoleServiceImpl.java
│   │       ├── utils/                  # 工具类
│   │       │   ├── JwtUtil.java                   # JWT工具
│   │       │   └── TokenContext.java              # Token上下文
│   │       └── RuleFrameApplication.java          # 应用启动类
│   ├── sql/                            # 数据库脚本
│   │   ├── init.sql                               # 初始化脚本
│   │   ├── update_menu_structure.sql              # 菜单结构更新
│   │   └── README_UPDATE.md
│   ├── pom.xml                         # 父POM
│   └── README.md
├── RuleFrame-Vue/                      # 前端项目
│   └── src/
│       ├── api/                        # API接口
│       │   ├── auth.ts                            # 认证API
│       │   ├── menu.ts                            # 菜单API
│       │   ├── request.ts                         # 请求封装
│       │   ├── role.ts                            # 角色API
│       │   ├── system.ts                          # 系统API
│       │   ├── types.ts                           # 类型定义
│       │   └── user.ts                            # 用户API
│       ├── layouts/                      # 布局组件
│       │   └── MainLayout.vue
│       ├── router/                       # 路由配置
│       │   └── index.ts
│       ├── stores/                       # 状态管理
│       │   ├── app.ts
│       │   └── auth.ts
│       └── views/                        # 页面组件
│           ├── ruleEngine/
│           │   └── RuleElement.vue                # 规则元素
│           ├── system/
│           │   ├── Menus.vue                      # 菜单管理
│           │   ├── Roles.vue                      # 角色管理
│           │   ├── Settings.vue                   # 系统设置
│           │   └── Users.vue                      # 用户管理
│           ├── Dashboard.vue                      # 仪表盘
│           ├── Login.vue                          # 登录页
│           ├── Profile.vue                        # 个人资料
│           └── Rules.vue                          # 规则管理
└── RuleFrame.code-workspace
```

## 🚀 快速开始

### 环境要求

- JDK 21+
- Maven 3.6+
- MySQL 8.0+
- Node.js 18+
- Yarn 或 npm

### 后端启动

1. **初始化数据库**
```bash
mysql -u root -p < sql/init.sql
```

2. **修改配置**
编辑 `rule-frame-web/src/main/resources/application.yml`，配置数据库连接：
```yaml
spring:
  datasource:
    url: jdbc:mysql://your-host:3306/ruleframe?useUnicode=true&characterEncoding=utf8
    username: your-username
    password: your-password
```

3. **编译项目**
```bash
mvn clean install
```

4. **启动应用**
```bash
cd rule-frame-web
mvn spring-boot:run
```

应用将在 `http://localhost:28080` 启动

### 前端启动

```bash
cd RuleFrame-Vue
yarn install  # 或 npm install
yarn dev      # 或 npm run dev
```

前端将在 `http://localhost:5173` 启动

### 默认账号

- 用户名: `admin`
- 密码: `admin123`

## 📐 架构设计

### 核心架构

```
┌─────────────────────────────────────────────────────────┐
│                     Web Layer (rule-frame-web)           │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌─────────┐ │
│  │Controller│  │ Service  │  │  Mapper  │  │  Entity │ │
│  └──────────┘  └──────────┘  └──────────┘  └─────────┘ │
└─────────────────────────┬───────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────┐
│                  Core Layer (rule-frame-core)            │
│  ┌──────────────────────────────────────────────────┐   │
│  │              RuleExecutor                        │   │
│  │  ┌─────────┐  ┌──────────┐  ┌────────────────┐  │   │
│  │  │RuleGroup│→ │GroupEval │→ │ExecStrategy    │  │   │
│  │  └─────────┘  └──────────┘  └────────────────┘  │   │
│  │       ↓                                          │   │
│  │  ┌──────────────────────────────────────────┐   │   │
│  │  │              Rule                        │   │   │
│  │  │  ┌──────────┐  ┌──────────────────────┐ │   │   │
│  │  │  │Condition │→ │Leaf/CompositeCondition│ │   │   │
│  │  │  └──────────┘  └──────────────────────┘ │   │   │
│  │  └──────────────────────────────────────────┘   │   │
│  └──────────────────────────────────────────────────┘   │
│  ┌────────────┐  ┌──────────┐  ┌──────────────────┐    │
│  │FactContext │  │Converter │  │OperatorRegistry  │    │
│  └────────────┘  └──────────┘  └──────────────────┘    │
│  ┌────────────┐  ┌──────────┐  ┌──────────────────┐    │
│  │ConfigLoader│  │Parser    │  │RuleRepository    │    │
│  └────────────┘  └──────────┘  └──────────────────┘    │
└─────────────────────────────────────────────────────────┘
```

### 规则执行流程

```
1. RuleExecutor.execute(groupKey, facts)
   ↓
2. RuleRepository.getRuleGroup(groupKey)
   ↓
3. GroupEvaluator.evaluate(ruleGroup, factContext)
   ↓
4. ExecutionStrategy.execute(ruleGroup, factContext)
   ↓
5. 遍历规则列表
   ↓
6. Rule.getCodiction().evaluate(factContext)
   ↓
7. Condition.evaluate() → LeafCondition / CompositeCondition
   ↓
8. 返回 GroupResult / ExecutionReport
```

## 📊 功能模块

### ✅ 已完成

| 模块 | 功能 | 状态 | 说明 |
|------|------|------|------|
| **基础设施** | 项目结构 | ✅ | 多模块Maven项目 |
| | Spring Boot集成 | ✅ | 3.4.0版本 |
| | MyBatis-Plus集成 | ✅ | ORM框架 |
| | MySQL配置 | ✅ | 数据库连接 |
| | JWT认证 | ✅ | Token生成与验证 |
| | 跨域配置 | ✅ | CORS支持 |
| **用户管理** | 用户CRUD | ✅ | 增删改查 |
| | 用户登录 | ✅ | 认证接口 |
| | 角色关联 | ✅ | 用户角色绑定 |
| **角色管理** | 角色CRUD | ✅ | 增删改查 |
| | 菜单关联 | ✅ | 角色菜单绑定 |
| **菜单管理** | 菜单CRUD | ✅ | 增删改查 |
| | 树形结构 | ✅ | 父子菜单 |
| | 权限控制 | ✅ | 权限标识 |
| **规则引擎** | 条件接口 | ✅ | Condition接口定义 |
| | 规则组 | ✅ | RuleGroup定义 |
| | 执行策略 | ✅ | 3种策略实现 |
| | 事实上下文 | ✅ | Map/Bean/Composite |
| | 类型转换 | ✅ | ConverterRegistry |
| | 路径解析 | ✅ | PathResolver |
| **前端** | 登录页面 | ✅ | JWT认证 |
| | 仪表盘 | ✅ | 统计展示 |
| | 用户管理 | ✅ | 列表/编辑 |
| | 角色管理 | ✅ | 列表/编辑 |
| | 菜单管理 | ✅ | 树形展示 |
| | 布局框架 | ✅ | MainLayout |

### 🚧 开发中

| 模块 | 功能 | 进度 | 说明 |
|------|------|------|------|
| **规则引擎** | RuleExecutor执行逻辑 | 🚧 30% | TODO标记 |
| | ConditionBuilder | 🚧 20% | 流式构建器 |
| | LeafCondition评估 | 🚧 0% | 叶子条件实现 |
| | CompositeCondition | 🚧 0% | 组合条件实现 |
| | OperatorRegistry | 🚧 0% | 运算符注册 |
| | RuleConfigLoader | 🚧 0% | 配置加载实现 |
| | RuleRepository | 🚧 0% | 仓库实现 |
| **规则管理** | 规则CRUD接口 | 🚧 0% | REST API |
| | 规则配置页面 | 🚧 0% | 可视化编辑 |
| | 规则测试 | 🚧 0% | 在线测试 |

### 📋 待开发

| 模块 | 功能 | 优先级 | 说明 |
|------|------|--------|------|
| **规则引擎** | 规则优先级 | P0 | 规则执行顺序 |
| | 失败动作 | P0 | FailureAction |
| | 执行监听器 | P1 | ExecutionListener |
| | 执行报告 | P1 | ExecutionReport |
| | 规则缓存 | P1 | 性能优化 |
| | 热加载 | P2 | 动态更新 |
| **配置管理** | JSON文件加载 | P0 | 文件配置 |
| | 数据库加载 | P0 | DB配置 |
| | 配置解析 | P0 | DTO转换 |
| **运算符** | 比较运算符 | P0 | =, !=, >, < |
| | 逻辑运算符 | P0 | AND, OR, NOT |
| | 范围运算符 | P1 | BETWEEN, IN |
| | 字符串运算符 | P1 | CONTAINS, MATCH |
| **前端** | 规则编辑器 | P0 | 可视化编辑 |
| | 条件构建器 | P0 | 拖拽构建 |
| | 规则测试面板 | P1 | 在线测试 |
| | 执行日志 | P1 | 结果查看 |
| **测试** | 单元测试 | P1 | Core模块 |
| | 集成测试 | P1 | Web模块 |
| | 性能测试 | P2 | 压力测试 |

## 🔌 API接口

### 认证接口

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/auth/login` | 用户登录 | ❌ |
| POST | `/api/auth/logout` | 用户登出 | ✅ |
| GET | `/api/auth/info` | 获取用户信息 | ✅ |

### 用户管理

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/users` | 用户列表 | ✅ |
| GET | `/api/users/{id}` | 用户详情 | ✅ |
| POST | `/api/users` | 创建用户 | ✅ |
| PUT | `/api/users/{id}` | 更新用户 | ✅ |
| DELETE | `/api/users/{id}` | 删除用户 | ✅ |

### 角色管理

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/roles` | 角色列表 | ✅ |
| GET | `/api/roles/{id}` | 角色详情 | ✅ |
| POST | `/api/roles` | 创建角色 | ✅ |
| PUT | `/api/roles/{id}` | 更新角色 | ✅ |
| DELETE | `/api/roles/{id}` | 删除角色 | ✅ |

### 菜单管理

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/menus` | 菜单列表 | ✅ |
| GET | `/api/menus/tree` | 菜单树 | ✅ |
| POST | `/api/menus` | 创建菜单 | ✅ |
| PUT | `/api/menus/{id}` | 更新菜单 | ✅ |
| DELETE | `/api/menus/{id}` | 删除菜单 | ✅ |

### 规则引擎

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/rules/execute` | 执行规则 | ❌ |
| POST | `/api/rules/context/execute` | 执行上下文 | ❌ |
| GET | `/api/rules/health` | 健康检查 | ❌ |

## 💾 数据库设计

### 核心表

- **sys_user**: 用户表
- **sys_role**: 角色表
- **sys_menu**: 菜单表
- **sys_user_role**: 用户角色关联表
- **sys_role_menu**: 角色菜单关联表

### 待设计表

- **rule_group**: 规则组表
- **rule**: 规则表
- **rule_condition**: 规则条件表
- **rule_element**: 规则元素表
- **rule_config**: 规则配置表

## 🧪 测试

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定模块测试
cd rule-frame-core
mvn test

# 运行特定测试类
mvn test -Dtest=RuleEngineTest
```

### 测试覆盖

- [ ] Core模块单元测试
- [ ] Web模块集成测试
- [ ] 控制器测试
- [ ] 服务层测试
- [ ] 条件引擎测试
- [ ] 执行策略测试

## 📝 开发规范

### 代码规范

- 使用 Lombok 简化POJO代码
- 遵循阿里巴巴Java开发规范
- 统一使用 ApiResponse 封装响应
- 使用 @RequiredArgsConstructor 注入依赖

### Git规范

- `feat`: 新功能
- `fix`: 修复bug
- `refactor`: 重构
- `docs`: 文档
- `test`: 测试
- `chore`: 构建/工具

### 提交示例

```bash
feat: 添加规则执行器实现
fix: 修复条件评估空指针问题
refactor: 重构转换器注册逻辑
docs: 更新API文档
```

## 🐛 已知问题

1. **RuleExecutor** 执行逻辑未实现 (TODO)
2. **ConditionBuilder** 流式构建器未完成 (TODO)
3. **LeafCondition** 评估逻辑未实现 (TODO)
4. **OperatorRegistry** 接口为空
5. **RuleConfigLoader** 加载器未实现

## 📌 后续计划

### V1.1 (下一个版本)

- [ ] 完成RuleExecutor核心执行逻辑
- [ ] 实现ConditionBuilder流式构建器
- [ ] 完成LeafCondition和CompositeCondition
- [ ] 实现OperatorRegistry和运算符
- [ ] 添加规则CRUD接口

### V1.2

- [ ] 规则可视化编辑器
- [ ] 条件拖拽构建
- [ ] 规则在线测试
- [ ] 执行日志查看

### V2.0

- [ ] 规则热加载
- [ ] 分布式支持
- [ ] 规则版本管理
- [ ] 性能监控

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'feat: Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 📄 许可证

[待定]

## 👥 团队

[待定]

---

**最后更新**: 2026-04-16

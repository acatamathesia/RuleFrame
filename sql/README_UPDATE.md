# 菜单树形结构更新说明

## 概述
本次更新将菜单结构调整为树形菜单，将角色管理、菜单管理和系统设置放到"系统管理"菜单下。

## 数据库变更

### 1. 表结构变更
- ✅ `sys_menu` 表已包含 `parent_id` 字段，支持树形结构
- ✅ Menu 实体类已添加 `children` 字段（不映射到数据库）

### 2. 菜单数据变更

#### 更新前的菜单结构（扁平结构）
```
- 系统管理 (parent_id=0)
- 仪表盘 (parent_id=0)
- 规则管理 (parent_id=0)
- 用户管理 (parent_id=0)
- 角色管理 (parent_id=0)
- 菜单管理 (parent_id=0)
- 系统设置 (parent_id=0)
```

#### 更新后的菜单结构（树形结构）
```
- 仪表盘 (parent_id=0)
- 规则管理 (parent_id=0)
- 用户管理 (parent_id=0)
- 系统管理 (parent_id=0, menu_type=1-目录)
  ├─ 角色管理 (parent_id=系统管理的ID)
  ├─ 菜单管理 (parent_id=系统管理的ID)
  └─ 系统设置 (parent_id=系统管理的ID)
```

## 更新步骤

### 方式一：执行更新脚本（推荐，适用于已有数据库）

```bash
mysql -u root -p < d:/project/RuleFrameProject/RuleFrame/sql/update_menu_structure.sql
```

或者在 MySQL 客户端中执行：
```sql
source d:/project/RuleFrameProject/RuleFrame/sql/update_menu_structure.sql
```

### 方式二：重新初始化数据库（适用于新环境）

```bash
mysql -u root -p < d:/project/RuleFrameProject/RuleFrame/sql/init.sql
```

### 方式三：手动更新

如果数据库中已有自定义菜单数据，建议手动更新：

```sql
USE ruleframe;

-- 1. 查看当前系统管理菜单的ID
SELECT id, menu_name, menu_code FROM sys_menu WHERE menu_code = 'system';

-- 2. 更新角色管理、菜单管理、系统设置的 parent_id
-- 假设系统管理的ID为4，请根据实际情况修改
UPDATE sys_menu SET parent_id = 4 WHERE menu_code IN ('roles', 'menus', 'settings');

-- 3. 验证更新结果
SELECT id, parent_id, menu_name, menu_code, menu_type, sort 
FROM sys_menu 
WHERE deleted = 0 
ORDER BY sort;
```

## 前端变更

### 1. 侧边栏菜单（MainLayout.vue）
- ✅ 使用 `el-sub-menu` 组件创建树形菜单
- ✅ 系统管理作为父级菜单，包含角色管理、菜单管理、系统设置

### 2. 路由配置（router/index.ts）
- ✅ 系统管理改为嵌套路由
- ✅ 角色管理、菜单管理、系统设置作为系统管理的子路由
- ✅ 路由路径：`/system/roles`, `/system/menus`, `/system/settings`

### 3. 菜单实体类（Menu.java）
- ✅ 添加 `children` 字段，支持树形结构返回
- ✅ 使用 `@TableField(exist = false)` 标记不映射到数据库

## 验证步骤

### 1. 验证数据库
```sql
-- 查看菜单树形结构
SELECT 
    id,
    parent_id,
    menu_name,
    menu_code,
    menu_type,
    path,
    icon,
    sort
FROM sys_menu
WHERE deleted = 0
ORDER BY parent_id, sort;
```

预期结果：
```
id | parent_id | menu_name  | menu_code | menu_type | path            | icon
---|-----------|------------|-----------|-----------|-----------------|-------------
1  | 0         | 仪表盘     | dashboard | 2         | /dashboard      | DataAnalysis
2  | 0         | 规则管理   | rules     | 2         | /rules          | Document
3  | 0         | 用户管理   | users     | 2         | /users          | User
4  | 0         | 系统管理   | system    | 1         | /system         | Setting
5  | 4         | 角色管理   | roles     | 2         | /system/roles   | UserFilled
6  | 4         | 菜单管理   | menus     | 2         | /system/menus   | Menu
7  | 4         | 系统设置   | settings  | 2         | /system/settings| Setting
```

### 2. 验证后端 API
访问：`http://localhost:8080/api/menus/tree`

预期返回树形结构：
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "parentId": 0,
      "menuName": "仪表盘",
      "menuCode": "dashboard",
      ...
    },
    {
      "id": 4,
      "parentId": 0,
      "menuName": "系统管理",
      "menuCode": "system",
      "children": [
        {
          "id": 5,
          "parentId": 4,
          "menuName": "角色管理",
          ...
        },
        ...
      ]
    }
  ]
}
```

### 3. 验证前端页面
1. 启动前端项目：`yarn dev`
2. 访问：`http://localhost:5173`
3. 检查左侧菜单是否显示树形结构
4. 点击"系统管理"是否展开子菜单
5. 点击各子菜单是否正常跳转

## 注意事项

1. **备份数据**：执行更新脚本前，请务必备份数据库
2. **parent_id**：子菜单的 parent_id 必须指向系统管理菜单的实际ID
3. **菜单类型**：
   - `menu_type = 1`：目录（父级菜单）
   - `menu_type = 2`：菜单（可点击跳转）
   - `menu_type = 3`：按钮（权限控制）
4. **角色菜单关联**：更新脚本会重新为超级管理员分配所有菜单权限
5. **自定义菜单**：如果有自定义菜单数据，请手动调整 parent_id

## 回滚方案

如果需要回滚到扁平结构：

```sql
-- 将所有菜单的 parent_id 设置为 0
UPDATE sys_menu SET parent_id = 0 WHERE deleted = 0;
```

## 技术细节

### MenuService 中的树形构建逻辑
```java
private List<Menu> buildMenuTree(List<Menu> menus, Long parentId) {
    return menus.stream()
            .filter(menu -> menu.getParentId().equals(parentId))
            .peek(menu -> {
                List<Menu> children = buildMenuTree(menus, menu.getId());
                menu.setChildren(children.isEmpty() ? null : children);
            })
            .collect(Collectors.toList());
}
```

这个方法会递归构建菜单树，自动将子菜单设置到父菜单的 `children` 属性中。

## 完成标志

- ✅ 数据库菜单数据更新为树形结构
- ✅ Menu 实体类添加 children 字段
- ✅ 后端 API 返回树形菜单数据
- ✅ 前端侧边栏显示树形菜单
- ✅ 前端路由配置为嵌套路由
- ✅ 所有功能正常运行

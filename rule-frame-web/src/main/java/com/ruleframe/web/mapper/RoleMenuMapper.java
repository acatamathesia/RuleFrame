package com.ruleframe.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruleframe.web.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色菜单关联 Mapper 接口
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
}

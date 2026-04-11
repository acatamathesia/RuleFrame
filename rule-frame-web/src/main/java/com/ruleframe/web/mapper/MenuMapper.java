package com.ruleframe.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruleframe.web.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单 Mapper 接口
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据角色ID查询菜单列表
     */
    List<Menu> selectMenusByRoleId(Long roleId);

    /**
     * 根据用户ID查询菜单列表
     */
    List<Menu> selectMenusByUserId(Long userId);
}

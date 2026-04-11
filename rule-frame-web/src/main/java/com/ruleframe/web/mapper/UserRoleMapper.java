package com.ruleframe.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruleframe.web.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色关联 Mapper 接口
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
}

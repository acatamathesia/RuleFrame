package com.ruleframe.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruleframe.web.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}

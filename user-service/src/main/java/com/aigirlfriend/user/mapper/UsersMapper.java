package com.aigirlfriend.user.mapper;


import com.aigirlfriend.user.domain.po.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户基本信息表 Mapper 接口
 * </p>
 *
 * @author hrhr74
 * @since 2025-07-16
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

}

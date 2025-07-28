package com.aigirlfriend.user.service;


import com.aigirlfriend.commen.utils.Result;
import com.aigirlfriend.user.domain.dto.UserDTO;
import com.aigirlfriend.user.domain.dto.UserLoginDTO;
import com.aigirlfriend.user.domain.po.Users;
import com.aigirlfriend.user.domain.vo.UserLoginVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户基本信息表 服务类
 * </p>
 *
 * @author hrhr74
 * @since 2025-07-16
 */
public interface IUsersService extends IService<Users> {

    Result register(UserDTO userDTO);

    Result<UserLoginVO> login(UserLoginDTO userLoginDTO);

    Result modify(UserDTO userDTO);
}

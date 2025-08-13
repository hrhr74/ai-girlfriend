package com.aigirlfriend.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.DigestUtil;
import com.aigirlfriend.commen.constant.TokenKeyConstant;
import com.aigirlfriend.commen.utils.Result;
import com.aigirlfriend.user.domain.dto.UserDTO;
import com.aigirlfriend.user.domain.dto.UserLoginDTO;
import com.aigirlfriend.user.domain.po.Users;
import com.aigirlfriend.user.domain.vo.UserLoginVO;
import com.aigirlfriend.user.mapper.UsersMapper;
import com.aigirlfriend.user.service.IUsersService;
import com.aigirlfriend.user.utils.JwtTool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;



/**
 * <p>
 * 用户基本信息表 服务实现类
 * </p>
 *
 * @author hrhr74
 * @since 2025-07-16
 */
@Service
@RequiredArgsConstructor
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    private final JwtTool jwtTool;
    /**
     * 用户注册
     * @param userDTO
     * @return
     */

    @Override
    public Result register(UserDTO userDTO) {
        if(userDTO == null){
            return Result.error("输入数据不正常！");
        }
        String password = userDTO.getPassword();
        if(password == null || password.isEmpty()){
            return Result.error("密码有误!");
        }
        //密码加密
        userDTO.setPassword(DigestUtil.md5Hex(password));
        //检查用户名是否已经存在
        if(lambdaQuery().eq(Users::getUsername,userDTO.getUsername()).exists()){
            return Result.error("用户已经存在！");
        }
        //转换为Users对象写入数据库
        Users users = BeanUtil.copyProperties(userDTO, Users.class);
        String username = users.getUsername();
        users.setUsername("@" + username);
        users.setCreateAt(LocalDateTime.now());
        users.setUpdateAt(LocalDateTime.now());

        if(userDTO.getNickname() == null || userDTO.getNickname().isEmpty()){
            //昵称为空则设置默认昵称
            users.setNickname("user_"+UUID.randomUUID().toString(true).substring(0,8));
        }
        save(users);
        return Result.ok();
    }

    /**
     * 用户用户名或id登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public Result<UserLoginVO> login(UserLoginDTO userLoginDTO) {
        if(userLoginDTO == null){
            return  Result.error("未知错误！");
        }
        //校验id是否都为空
        String username = userLoginDTO.getUsername();
        if(username == null || username.isEmpty()){
            return Result.error("账号异常！");
        }
        //加密密码
        String password = userLoginDTO.getPassword();
        if(password == null || password.isEmpty()){
            return Result.error("密码格式有问题！");
        }
        password = DigestUtil.md5Hex(password);
        //查询数据库
        Users user = lambdaQuery().eq(Users::getUsername, "@"+username).one();
        if(user == null){
            return Result.error("用户不存在！");
        }
        if(!user.getPassword().equals(password)){
            return Result.error("密码错误！");
        }
        //生成token
        String token = jwtTool.createToken(user.getId(), Duration.ofDays(TokenKeyConstant.TTL));
        //返回给前端
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setId(user.getId());
        userLoginVO.setUsername(username);
        userLoginVO.setToken(token);
        return Result.ok(userLoginVO);
    }

    /**
     * 修改用户
     * @param userDTO
     * @return
     */
    @Override
    public Result modify(UserDTO userDTO) {
        if(userDTO == null){
            return Result.error("输入数据异常！");
        }
        Users users = BeanUtil.copyProperties(userDTO, Users.class);
        users.setUpdateAt(LocalDateTime.now());
        boolean b = updateById(users);
        return  b ? Result.ok() : Result.error("更新失败!");
    }
}

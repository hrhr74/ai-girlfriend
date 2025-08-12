package com.aigirlfriend.user.controller;

import cn.hutool.core.bean.BeanUtil;
import com.aigirlfriend.commen.utils.Result;
import com.aigirlfriend.user.domain.dto.UserDTO;
import com.aigirlfriend.user.domain.dto.UserLoginDTO;
import com.aigirlfriend.user.domain.po.Users;
import com.aigirlfriend.user.domain.vo.UserLoginVO;
import com.aigirlfriend.user.service.IUsersService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUsersService usersService;
    /**
     * 用户注册账号
     */
    @PostMapping("register")
    public Result register(@RequestBody UserDTO userDTO){
        return usersService.register(userDTO);
    }
    /**
     * 用户登录,可以用户名密码登录，或者账号密码登录
     */
    @PostMapping("login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        return usersService.login(userLoginDTO);
    }

    /**
     * 修改用户
     */
    @PutMapping
    public Result update(@RequestBody UserDTO userDTO){
        return usersService.modify(userDTO);
    }
    /**
     * 根据id查询用户
     */
    @PostMapping("id")
    public  Result<UserDTO> getById(){
        Long userId = 1L;//TODO UserContext.getUser()
        Users byId = usersService.getById(userId);
        return Result.ok(BeanUtil.copyProperties(byId, UserDTO.class));
    }

}

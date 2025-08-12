package com.aigirlfriend.character.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aigirlfriend.api.domain.vo.AiCharactersVO;
import com.aigirlfriend.character.domain.dto.AiCharactersDTO;
import com.aigirlfriend.character.domain.po.AiCharacters;
import com.aigirlfriend.character.mapper.AiCharactersMapper;
import com.aigirlfriend.character.service.IAiCharactersService;
import com.aigirlfriend.commen.content.UserConstant;
import com.aigirlfriend.commen.utils.Result;
import com.aigirlfriend.commen.utils.UserContext;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AiCharactersServiceImpl extends ServiceImpl<AiCharactersMapper,AiCharacters> implements IAiCharactersService {

    /**
     * 获取用户Id
     * @return
     */
    private Long getUserId(){
        return UserContext.getUser();
    }

    /**
     * 新增用户
     * @param aiCharactersDTO
     * @return
     */
    @Override
    public Result addCharacter(AiCharactersDTO aiCharactersDTO) {
        if(aiCharactersDTO == null){
            return Result.error("传入参数不能为空！");
        }

        //获取用户id;
        Long userId = getUserId();

        if(userId == null){
            return Result.error("用户不存在！");
        }
        //检验用户想设置的女友名字是否已经存在
        String name = aiCharactersDTO.getName();
        AiCharacters one = lambdaQuery().eq(AiCharacters::getUserId, userId)
                .eq(name != null, AiCharacters::getName, name)
                .one();
        if(one != null){
            return Result.error("女友名字已存在！");
        }

        //将aiCharactersDTO转换为AiCharacters
        AiCharacters aiCharacters = new AiCharacters();
        BeanUtil.copyProperties(aiCharactersDTO,aiCharacters);

        //添加创建时间
        LocalDateTime now = LocalDateTime.now();

        //封装
        aiCharacters.setUserId(userId);
        aiCharacters.setCreatedAt(now);

        //存入数据库
        save(aiCharacters);
        return Result.ok("新增女友角色成功！");
    }

    /**
     * 查询用户设置的全部角色
     * @return
     */
    @Override
    public Result<List<AiCharactersVO>> listAiCharacters() {
        //获取用户Id
        Long userId = getUserId();
        if(userId == null){
            return Result.error("用户未登录！");
        }

        //查询
        List<AiCharacters> aiCharactersList = lambdaQuery().eq(AiCharacters::getUserId, userId).list();
        if(aiCharactersList.isEmpty()){
            return Result.ok(Collections.emptyList());
        }
        List<AiCharactersVO> aiCharactersVOList = new ArrayList<>();
        aiCharactersVOList = BeanUtil.copyToList(aiCharactersList, AiCharactersVO.class);

        //将查询结果返回
        return Result.ok(aiCharactersVOList);
    }

    /**
     * 更新角色
     * @param aiCharactersDTO
     * @return
     */
    @Override
    public Result updateCharacter(AiCharactersDTO aiCharactersDTO) {
        if(aiCharactersDTO == null){
            return Result.error("输入数据格式有问题");
        }
        AiCharacters aiCharacters = getById(aiCharactersDTO.getId());
        if(aiCharacters == null){
            return  Result.error("角色不存在！");
        }
        String name = aiCharacters.getName();

        //查看名字是否和其他角色重复
        boolean exist = lambdaQuery()
                .eq(AiCharacters::getName, aiCharactersDTO.getName())
                .ne(AiCharacters::getId, aiCharactersDTO.getId())
                .exists();
        if(!aiCharactersDTO.getName().equals(name)){
            if( exist) {
                return Result.error("名字与其他角色重复，换一个名字试试吧^^！");
            }
        }
        //修改
        updateById(BeanUtil.copyProperties(aiCharactersDTO, AiCharacters.class));
        return Result.ok("修改成功！");
    }

    /**
     * 设置默认角色
     * @param id
     * @return
     */
    @Override
    public Result setDeafult(Long id) {
        Long userId = getUserId();
        if(userId == null){
            return Result.error("用户未登录！");
        }

        //查询当前用户下是否有该id的角色
        AiCharacters aiCharacters = lambdaQuery().eq(AiCharacters::getUserId, userId)
                .eq(AiCharacters::getId, id)
                .one();
        if(aiCharacters == null){
            return Result.error("当前用户下不存在该角色！！！");
        }

        //查询当前是否有默认角色
        AiCharacters default_character = lambdaQuery().eq(AiCharacters::getUserId, userId)
                .eq(AiCharacters::getIsDefault, 1)
                .one();
        if(default_character != null) {
            //有默认角色，则将默认角色修改为非默认
            default_character.setIsDefault(false);
            updateById(default_character);
        }

        //设置默认角色
        aiCharacters.setIsDefault(true);
        updateById(aiCharacters);
        return Result.ok();
    }

    /**
     * 查询默认角色
     * @return
     */
    @Override
    public Result<AiCharactersVO> getDefault() {
        Long userId = getUserId();
        if(userId == null){
            return Result.error("用户未登录！");
        }

        AiCharacters default_character = lambdaQuery().eq(AiCharacters::getUserId, userId)
                .eq(AiCharacters::getIsDefault, 1)
                .one();

        if(default_character == null){
            //随机返回一个角色
            AiCharacters aiCharacters = lambdaQuery().eq(AiCharacters::getUserId, userId).
                    last("ORDER BY RAND() LIMIT 1")
                    .one();
            if (aiCharacters == null) {
                //返回系统默认角色
                aiCharacters = lambdaQuery().eq(AiCharacters::getUserId, UserConstant.Admin_ID)
                        .one();
            }
            AiCharactersVO aiCharactersVO = BeanUtil.copyProperties(aiCharacters, AiCharactersVO.class);
            return  Result.ok(aiCharactersVO);
        }
        return Result.ok(BeanUtil.copyProperties(default_character, AiCharactersVO.class));
    }
}

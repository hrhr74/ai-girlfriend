package com.aigirlfriend.character.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.aigirlfriend.character.domain.dto.AiCharactersDTO;
import com.aigirlfriend.character.domain.po.AiCharacters;
import com.aigirlfriend.character.domain.vo.AiCharactersVO;
import com.aigirlfriend.character.mapper.AiCharactersMapper;
import com.aigirlfriend.character.service.IAiCharactersService;
import com.aigirlfriend.commen.utils.Result;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
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
        return 1L;//TODO 获取用户Id
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
        Long userId = getUserId();//TODO UserContext.getUser();

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
}

package com.aigirlfriend.memory.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aigirlfriend.api.domain.query.MemoryQuery;
import com.aigirlfriend.commen.utils.Result;
import com.aigirlfriend.memory.domain.dto.MemoryDTO;
import com.aigirlfriend.memory.domain.po.Memory;
import com.aigirlfriend.memory.mapper.MemoryMapper;
import com.aigirlfriend.memory.service.IMemoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.aigirlfriend.api.constant.MemoryConstant.MEMORY_MAX_VALUE;

@Service
@RequiredArgsConstructor
public class MemoryServiceImpl extends ServiceImpl<MemoryMapper, Memory> implements IMemoryService{

    private final MemoryMapper memoryMapper;
    /**
     * 新增记忆
     * @param memoryDTO
     * @return
     */

    @Override
    public Result saveMemory(MemoryDTO memoryDTO) {
        //获取当前用户
        Long userId = 1L;//TODO Context.getUser()
        if(userId == null){
            return Result.error("用户未登录！");
        }

        //获取是否有该记忆
        if(memoryDTO == null || memoryDTO.getMemoryKey() == null || memoryDTO.getMemoryKey().isEmpty()){
            return Result.error("输入数据无效！");
        }
        String memoryKey = memoryDTO.getMemoryKey();
        if(lambdaQuery().eq(Memory::getUserId,userId).eq(Memory::getMemoryKey,memoryKey).exists()){
            return Result.error("目前记忆类型已存在！");
        }

        //查询记忆数量是否已经达到三个
        Long count = lambdaQuery().eq(Memory::getUserId, userId).count();
        if(count >= MEMORY_MAX_VALUE){
            return Result.error("新增失败，当前用户的记忆数量以达到最大值！");
        }

        //新增记忆
        Memory memory = BeanUtil.copyProperties(memoryDTO, Memory.class);
        memory.setCreatedAt(LocalDateTime.now());
        memory.setUpdatedAt(LocalDateTime.now());
        boolean save = save(memory);
        return save == true ? Result.ok() : Result.error("添加失败，请稍后再试！");
    }

    /**
     * 查询当前用户所有的记忆
     * @return
     */
    @Override
    public Result<List<MemoryQuery>> queryMemoryList() {
        Long userId = 1L;//TODO Context.getUser();
        if(userId == null){
            return Result.error("当前用户未登录！");
        }

        List<Memory> memoryList = lambdaQuery()
                .eq(Memory::getUserId, userId)
                .orderByDesc(Memory::getUpdatedAt)
                .list();
        if(memoryList.isEmpty()){
            return Result.ok(Collections.emptyList());
        }

        List<MemoryQuery> memoryQueries = BeanUtil.copyToList(memoryList, MemoryQuery.class);
        return Result.ok(memoryQueries);
    }

    /**
     * 删除记忆
     * @param id
     * @return
     */
    @Override
    public Result deleteMemory(Long id) {
        if(id == null){
            return Result.error("记忆不存在！");
        }

        boolean isRemove = removeById(id);
        return isRemove ? Result.ok() : Result.error("删除失败，请稍后再试！");
    }

    /**
     * 修改记忆
     * @param memoryDTO
     * @return
     */
    @Override
    public Result updateMemory(MemoryDTO memoryDTO) {
        if(memoryDTO == null){
            return Result.error("要修改对象不存在");
        }

        Memory memory = BeanUtil.copyProperties(memoryDTO, Memory.class);
        memory.setUpdatedAt(LocalDateTime.now());
        memoryMapper.updateMemory(memory);
        return Result.ok();
    }
}

package com.aigirlfriend.memory.controller;

import cn.hutool.core.bean.BeanUtil;
import com.aigirlfriend.api.domain.query.MemoryQuery;
import com.aigirlfriend.commen.utils.Result;
import com.aigirlfriend.memory.domain.dto.MemoryDTO;
import com.aigirlfriend.memory.service.IMemoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/memory")
@RequiredArgsConstructor
public class MemoryController {
    private final IMemoryService memoryService;

    /**
     * 新增记忆
     */
    @PostMapping
    public Result saveMemory(@RequestBody MemoryDTO memoryDTO){
        return memoryService.saveMemory(memoryDTO);
    }


    /**
     * 查询所有记忆
     */
    @GetMapping("list")
    public Result<List<MemoryQuery>> queryMemoryList(){
        return memoryService.queryMemoryList();
    }

    /**
     * 删除记忆
     */
    @DeleteMapping("{id}")
    public Result deleteMemory(@PathVariable("id") Long id){
        return memoryService.deleteMemory(id);
    }

    /**
     * 修改记忆
     */
    @PutMapping
    public Result updateMemory(@RequestBody MemoryDTO memoryDTO){
        return memoryService.updateMemory(memoryDTO);
    }
    /**
     * 根据id查询
     */
    @PostMapping("{id}")
    public Result<MemoryQuery> getById(@PathVariable("id") Long id){
        return Result.ok(BeanUtil.copyProperties(memoryService.getById(id),MemoryQuery.class));
    }
}

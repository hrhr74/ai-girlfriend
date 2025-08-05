package com.aigirlfriend.memory.service;

import com.aigirlfriend.api.domain.query.MemoryQuery;
import com.aigirlfriend.commen.utils.Result;
import com.aigirlfriend.memory.domain.dto.MemoryDTO;
import com.aigirlfriend.memory.domain.po.Memory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IMemoryService extends IService<Memory> {

    Result saveMemory(MemoryDTO memoryDTO);

    Result<List<MemoryQuery>> queryMemoryList();

    Result deleteMemory(Long id);

    Result updateMemory(MemoryDTO memoryDTO);
}

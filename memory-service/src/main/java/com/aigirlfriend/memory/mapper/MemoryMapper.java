package com.aigirlfriend.memory.mapper;

import com.aigirlfriend.memory.domain.po.Memory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemoryMapper extends BaseMapper<Memory> {
    void updateMemory(Memory memory);
}

package com.aigirlfriend.chat.mapper;


import com.aigirlfriend.chat.domain.po.ChatMessages;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatMessagesMapper extends BaseMapper<ChatMessages> {
}

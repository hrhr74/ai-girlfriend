package com.aigirlfriend.chat.service.impl;

import com.aigirlfriend.chat.domain.dto.ChatMessagesDTO;
import com.aigirlfriend.chat.domain.po.ChatMessages;
import com.aigirlfriend.chat.mapper.ChatMessagesMapper;
import com.aigirlfriend.chat.service.IChatMessagesService;
import com.aigirlfriend.commen.utils.Result;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ChatMessagesServiceImpl extends ServiceImpl<ChatMessagesMapper, ChatMessages> implements IChatMessagesService {
    @Override
    public Result saveMessage(ChatMessagesDTO chatMessagesDTO) {
        return null;
    }
}

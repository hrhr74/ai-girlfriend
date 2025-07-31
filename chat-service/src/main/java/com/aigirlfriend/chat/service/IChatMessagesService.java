package com.aigirlfriend.chat.service;

import com.aigirlfriend.chat.domain.dto.ChatMessagesDTO;
import com.aigirlfriend.chat.domain.po.ChatMessages;
import com.aigirlfriend.commen.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IChatMessagesService extends IService<ChatMessages> {
    Result saveMessage(ChatMessagesDTO chatMessagesDTO);
}

package com.aigirlfriend.chat.service;

import com.aigirlfriend.api.domain.dto.ChatMessagesDTO;
import com.aigirlfriend.chat.domain.po.ChatMessages;
import com.aigirlfriend.chat.domain.vo.ChatMessagesVO;
import com.aigirlfriend.commen.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IChatMessagesService extends IService<ChatMessages> {
    Result saveMessage(ChatMessagesDTO chatMessagesDTO);

    Result<List<ChatMessagesVO>> queryMessages(Long id);

    Result deleteMessage(Long id);
}

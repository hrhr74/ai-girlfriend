package com.aigirlfriend.chat.controller;

import com.aigirlfriend.chat.domain.dto.ChatMessagesDTO;
import com.aigirlfriend.chat.service.IChatMessagesService;
import com.aigirlfriend.commen.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class ChatMessagesController {
    private IChatMessagesService chatMessagesService;
    /**
     * 保存消息
     */
    public Result saveMessage(ChatMessagesDTO chatMessagesDTO){
        return chatMessagesService.saveMessage(chatMessagesDTO);
    }

    /**
     * 查询消息
     */


    /**
     * 删除消息
     */
}

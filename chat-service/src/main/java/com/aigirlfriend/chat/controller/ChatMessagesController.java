package com.aigirlfriend.chat.controller;

import com.aigirlfriend.api.domain.dto.ChatMessagesDTO;
import com.aigirlfriend.chat.domain.vo.ChatMessagesVO;
import com.aigirlfriend.chat.service.IChatMessagesService;
import com.aigirlfriend.commen.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class ChatMessagesController {
    private final IChatMessagesService chatMessagesService;
    /**
     * 保存消息
     */
    @PostMapping
    public Result saveMessage(@RequestBody ChatMessagesDTO chatMessagesDTO){
        return chatMessagesService.saveMessage(chatMessagesDTO);
    }

    /**
     * 根据用户和会话id查询消息
     */
    @PostMapping("{id}")
    public Result<List<ChatMessagesVO>> queryMessages(@PathVariable("id") Long id){
        return chatMessagesService.queryMessages(id);
    }
    /**
     * 删除消息
     */
    @DeleteMapping("{id}")
    public Result deleteMessage(@PathVariable("id") Long id){
        return chatMessagesService.deleteMessage(id);
    }
}

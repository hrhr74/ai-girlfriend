package com.aigirlfriend.chat.listeners;

import com.aigirlfriend.api.domain.dto.ChatMessagesDTO;
import com.aigirlfriend.api.domain.dto.ChatSessionDTO;
import com.aigirlfriend.chat.service.IChatMessagesService;
import com.aigirlfriend.chat.service.IChatSessionService;
import com.aigirlfriend.commen.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChatListeners{
    private final IChatSessionService sessionService;
    private final IChatMessagesService messagesService;

    //保存会话
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("session.queue"),
            exchange = @Exchange(value = "chat.exchange"),
            key = "session"
    ))
    public Long saveSession(ChatSessionDTO chatSessionDTO){
        Result<Long> longResult = sessionService.saveSession(chatSessionDTO);
        Long data = longResult.getData();
        return data;
    }
    //保存消息
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("message.queue"),
            exchange = @Exchange(value = "chat.exchange"),
            key = "message"
    ))
    public void saveMessage(ChatMessagesDTO chatMessageDTO){
        messagesService.saveMessage(chatMessageDTO);
    }
}

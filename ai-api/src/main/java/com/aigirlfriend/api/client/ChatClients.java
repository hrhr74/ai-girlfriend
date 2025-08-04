package com.aigirlfriend.api.client;

import com.aigirlfriend.api.domain.dto.ChatMessagesDTO;
import com.aigirlfriend.api.domain.dto.ChatSessionDTO;
import com.aigirlfriend.commen.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("chat-service")
public interface ChatClients {
    /**
     *session相关
     * */
    @PostMapping("/session")
    Result<Long> saveSession(@RequestBody ChatSessionDTO chatSessionDTO);


    /**
     * message相关
     */
    @PostMapping("/message")
    Result saveMessage(@RequestBody ChatMessagesDTO chatMessagesDTO);
}

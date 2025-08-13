package com.aigirlfriend.chat.domain.vo;

import com.aigirlfriend.chat.domain.po.ChatMessages;
import lombok.Data;

import java.util.List;

@Data
public class SessionMessagesVO {
    private Long id;

    private Long userId;

    private String sessionId;

    private String title;

    private List<ChatMessagesVO> messages;
}

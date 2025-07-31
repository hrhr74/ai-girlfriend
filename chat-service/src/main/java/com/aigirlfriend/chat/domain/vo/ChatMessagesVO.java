package com.aigirlfriend.chat.domain.vo;

import java.time.LocalDateTime;

public class ChatMessagesVO {
    private Long id;
    private Long sessionId;
    private Long userId;
    private String content;
    private Integer isUser;
    private LocalDateTime sentAt;
}

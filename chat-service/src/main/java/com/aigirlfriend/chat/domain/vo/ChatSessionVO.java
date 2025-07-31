package com.aigirlfriend.chat.domain.vo;

import lombok.Data;

@Data
public class ChatSessionVO {
    private Long id;

    private Long userId;

    private String sessionId;

    private String title;
}

package com.aigirlfriend.api.domain.dto;

import lombok.Data;

@Data
public class ChatMessagesDTO {
    private Long id;
    private Long sessionId;
    private String content;
    private Integer isUser;
}

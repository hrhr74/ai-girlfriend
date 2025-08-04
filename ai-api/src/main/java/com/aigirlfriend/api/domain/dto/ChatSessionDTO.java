package com.aigirlfriend.api.domain.dto;

import lombok.Data;

@Data
public class ChatSessionDTO {

    private Long id;

    private Long userId;

    private String sessionId;

    private String title;
}

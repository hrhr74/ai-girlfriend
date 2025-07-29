package com.aigirlfriend.chat.domain.po;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@TableName("chat_sessions")
@Data
public class ChatSession {
    private Long id;

    private Long userId;

    private String sessionId;

    private String title;


    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    private Boolean status;
}

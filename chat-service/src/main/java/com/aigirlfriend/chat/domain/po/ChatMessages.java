package com.aigirlfriend.chat.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_messages")
public class ChatMessages {
    private Long id;
    private Long sessionId;
    private Long userId;
    private String content;
    private Integer isUser;
    private LocalDateTime sentAt;
    @TableField("is_deleted")
    private Boolean deleted;
    private LocalDateTime deletedAt;
}

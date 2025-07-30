package com.aigirlfriend.character.domain.po;

import com.aigirlfriend.api.domain.po.Personality;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "ai_characters",autoResultMap = true)
public class AiCharacters {
    private Long id;
    private String name;
    private String description;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Personality personality;

    private String avatar;
    private Long userId;
    private Boolean isDefault;
    private LocalDateTime createdAt;
}

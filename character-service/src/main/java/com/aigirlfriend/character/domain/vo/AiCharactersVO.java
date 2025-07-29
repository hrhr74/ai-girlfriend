package com.aigirlfriend.character.domain.vo;

import com.aigirlfriend.character.domain.po.Personality;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

@Data
public class AiCharactersVO {
    private Long id;

    private String name;

    private String description;

    private Personality personality;

    private String avatar;

    private Long userId;

    private Boolean isDefault;
}

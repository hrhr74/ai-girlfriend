package com.aigirlfriend.character.domain.dto;

import com.aigirlfriend.api.domain.po.Personality;
import lombok.Data;

@Data
public class AiCharactersDTO {
    private Long id;
    private String name;
    private String description;
    private Personality personality;
    private String avatar;
    private Boolean isDefault;
}

package com.aigirlfriend.api.domain.vo;


import com.aigirlfriend.api.domain.po.Personality;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiCharactersVO {
    private Long id;

    private String name;

    private String description;

    private Personality personality;

    private String avatar;

    private Long userId;

    private Boolean isDefault;
}

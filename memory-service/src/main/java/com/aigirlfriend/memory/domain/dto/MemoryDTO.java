package com.aigirlfriend.memory.domain.dto;

import lombok.Data;

@Data
public class MemoryDTO {
    private Long id;
    private Long userId;
    private String memoryKey;
    private String memoryValue;
    private Integer importance;
}

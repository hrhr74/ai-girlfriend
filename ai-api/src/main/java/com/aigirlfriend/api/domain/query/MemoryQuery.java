package com.aigirlfriend.api.domain.query;

import lombok.Data;

@Data
public class MemoryQuery {
    private Long id;
    private Long userId;
    private String memoryKey;
    private String memoryValue;
    private Integer importance;
}

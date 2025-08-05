package com.aigirlfriend.memory.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_memories")
public class Memory {
    private Long id;
    private Long userId;
    @TableField("memory_key")
    private String memoryKey;
    @TableField("memory_value")
    private String memoryValue;
    private Integer importance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

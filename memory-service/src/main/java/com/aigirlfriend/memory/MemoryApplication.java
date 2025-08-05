package com.aigirlfriend.memory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.aigirlfriend.memory.mapper")
@SpringBootApplication
public class MemoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemoryApplication.class, args);
    }
}

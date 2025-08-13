package com.aigirlfriend;

import com.aigirlfriend.api.config.FeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableFeignClients(value = "com.aigirlfriend.api.client",defaultConfiguration = FeignConfig.class)
@PropertySource("classpath:application.yaml")
public class AiGrilFriendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiGrilFriendApplication.class, args);
    }

}

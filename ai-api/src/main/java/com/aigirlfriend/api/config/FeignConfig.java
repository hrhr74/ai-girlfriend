package com.aigirlfriend.api.config;

import com.aigirlfriend.commen.utils.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class FeignConfig {
    @Bean
    public RequestInterceptor userInfoRequestTemplate(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                Long userId = UserContext.getUser();
                if(userId != null){
                    requestTemplate.header("user-info",userId.toString());
                }
            }
        };
    }
}

package com.aigirlfriend.domain.vo;

import lombok.Data;

import java.util.List;
/*
dsapi响应
 */
@Data
public class DSChatResponse {
    private List<Choice> choices;

    @Data
    public static class Choice{


        private Message message;
        @Data
        public static class Message{
            private String role;

            private String content;


        }

    }
}

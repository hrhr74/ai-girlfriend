package com.aigirlfriend.domain.vo;

import lombok.Data;

import java.util.List;
/*
dsapi的请求体
 */
@Data
public class DSChatRequest {
    //模型
   private String model;


    //消息
   private List<Message> messages;
   @Data
   public static class Message{
       public Message(String role, String content) {
           this.role = role;
           this.content = content;
       }

       private String role;//消息的角色
       private String content;//消息的内容
   }
}

package com.aigirlfriend.service.impl;

import com.aigirlfriend.domain.vo.DSChatRequest;
import com.aigirlfriend.domain.vo.DSChatResponse;
import com.aigirlfriend.service.IDeepSeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeepSeekServiceImpl implements IDeepSeekService {

    @Value("${deepseek.api.key}")
    private String api_key;
    @Value("${deepseek.api.url}")
    private String api_url;

    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public String callDS(String userMessage) {
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");
        headers.set("Authorization","Bearer " + api_key);

        List<DSChatRequest.Message> messages = new ArrayList<>();
        //初始化角色风格
        messages.add(new DSChatRequest.Message("system","你是一只猫娘"));
        messages.add(new DSChatRequest.Message("user",userMessage));
        //请求体
        DSChatRequest request = new DSChatRequest();
        request.setModel("deepseek-chat");

        request.setMessages(messages);
        //发送请求
        HttpEntity<DSChatRequest> entity = new HttpEntity<>(request,headers);
        ResponseEntity<DSChatResponse> response = restTemplate.
                exchange(api_url, HttpMethod.POST,entity, DSChatResponse.class);
        //解析响应
        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null){
            System.out.println("已发送");
            return response.getBody().getChoices().get(0).getMessage().getContent();
        }else{
            throw new RuntimeException("获取deepseekApi失败 " + response.getStatusCode());
        }

    }
}

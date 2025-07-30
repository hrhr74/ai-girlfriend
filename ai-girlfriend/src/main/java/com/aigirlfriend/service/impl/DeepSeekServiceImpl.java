package com.aigirlfriend.service.impl;

import com.aigirlfriend.api.client.CharactersClient;
import com.aigirlfriend.api.domain.po.Personality;
import com.aigirlfriend.api.domain.vo.AiCharactersVO;
import com.aigirlfriend.commen.utils.Result;
import com.aigirlfriend.domain.vo.DSChatRequest;
import com.aigirlfriend.domain.vo.DSChatResponse;
import com.aigirlfriend.service.IDeepSeekService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class DeepSeekServiceImpl implements IDeepSeekService {

    @Value("${deepseek.api.key}")
    private String api_key;
    @Value("${deepseek.api.url}")
    private String api_url;

    private final RestTemplate restTemplate = new RestTemplate();

    private final CharactersClient charactersClient;

    public String getSystem(){
        AiCharactersVO aiCharactersVO = charactersClient.getDefault().getData();
        if(aiCharactersVO == null){
            throw  new RuntimeException("角色获取错误！");
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(aiCharactersVO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("角色序列化失败！",e);
        }
    }
    @Override
    public String callDS(String userMessage) {
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");
        headers.set("Authorization","Bearer " + api_key);

        List<DSChatRequest.Message> messages = new ArrayList<>();
        //初始化角色风格
            //获取用户的默认性格
        messages.add(new DSChatRequest.Message("system",getSystem()));
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

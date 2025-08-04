package com.aigirlfriend.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.aigirlfriend.api.client.CharactersClient;
import com.aigirlfriend.api.client.ChatClients;
import com.aigirlfriend.api.domain.dto.ChatMessagesDTO;
import com.aigirlfriend.api.domain.dto.ChatSessionDTO;
import com.aigirlfriend.api.domain.vo.AiCharactersVO;
import com.aigirlfriend.commen.utils.Result;
import com.aigirlfriend.domain.vo.DSChatRequest;
import com.aigirlfriend.domain.vo.DSChatResponse;
import com.aigirlfriend.service.IDeepSeekService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.aigirlfriend.constant.DSConstant.*;

@Service
@RequiredArgsConstructor
public class DeepSeekServiceImpl implements IDeepSeekService {

    @Value("${deepseek.api.key}")
    private String api_key;
    @Value("${deepseek.api.url}")
    private String api_url;
    private final RestTemplate restTemplate = new RestTemplate();

    private final CharactersClient charactersClient;

    private final ChatClients chatClients;
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

    /**
     * 请求ds
     * @param role
     * @param userMessage
     * @return
     */
    public String callDS(String role,String userMessage) {
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");
        headers.set("Authorization","Bearer " + api_key);

        List<DSChatRequest.Message> messages = new ArrayList<>();
        //初始化角色风格
            //获取用户的默认性格
        messages.add(new DSChatRequest.Message("system",role));
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
            return ERROR_RETURN;
        }

    }

    /**
     * 保存会话
     * @param userMessage
     * @return
     */
    public ChatSessionDTO setChatSessionDTO(String userMessage){
        ChatSessionDTO chatSessionDTO = new ChatSessionDTO();
        chatSessionDTO.setTitle(setTitle(userMessage));
        chatSessionDTO.setUserId(getUserId());
        return chatSessionDTO;
    }

    /**
     * 保存消息
     *
     * @param message
     * @param curId
     * @return
     */

    public ChatMessagesDTO setChatMessageDTO(String message, Integer isUser, Long curId){
        ChatMessagesDTO chatMessagesDTO = new ChatMessagesDTO();
        chatMessagesDTO.setContent(message);
        chatMessagesDTO.setIsUser(isUser);
        chatMessagesDTO.setSessionId(curId);
        return chatMessagesDTO;
    }

    /**
     * 设置标题
     * @param prompt
     * @return
     */
    public String setTitle(String prompt){

        String title = callDS(TITLE_PROMPT, prompt);
        title = title.trim();
        if(title.length() > 20){
            title = title.substring(0,20);
        }
        return title;
    }

    /**
     * 获取用户id
     * @return
     */
    public Long getUserId(){
        return 1L;//TODO Context,getUser();
    }
    /**
     * 发送消息
     * @param userMessage
     * @param sessionId
     * @return
     */
    @Override
    public String sendMsg(String userMessage, Long sessionId) {
        Long curId = sessionId;
        if(sessionId == null){
            ChatSessionDTO chatSessionDTO = setChatSessionDTO(userMessage);
            Result<Long> longResult = chatClients.saveSession(chatSessionDTO);
            if(longResult != null){
                curId = longResult.getData();
            }
        }
        String aiMessage = callDS(getSystem(), userMessage);
        if(!aiMessage.equals(ERROR_RETURN)){
            //发送消息成功，保存用户消息
            ChatMessagesDTO chatMessagesDTO = setChatMessageDTO(userMessage, USER_MSG,curId);
            chatClients.saveMessage(chatMessagesDTO);
            //保存ai消息
            ThreadUtil.sleep(1000);
            ChatMessagesDTO aiMessageDTO = setChatMessageDTO(aiMessage, AI_MSG,curId);
            chatClients.saveMessage(aiMessageDTO);
        }else{
            return "error";
        }
        return aiMessage;
    }
}

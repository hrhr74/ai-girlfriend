package com.aigirlfriend.service.impl;

import com.aigirlfriend.api.client.CharactersClient;
import com.aigirlfriend.api.client.ChatClients;
import com.aigirlfriend.api.client.MemoryClient;
import com.aigirlfriend.api.domain.dto.ChatMessagesDTO;
import com.aigirlfriend.api.domain.dto.ChatSessionDTO;
import com.aigirlfriend.api.domain.query.MemoryQuery;
import com.aigirlfriend.api.domain.vo.AiCharactersVO;
import com.aigirlfriend.commen.utils.Result;
import com.aigirlfriend.commen.utils.UserContext;
import com.aigirlfriend.domain.vo.DSChatRequest;
import com.aigirlfriend.domain.vo.DSChatResponse;
import com.aigirlfriend.service.IDeepSeekService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.aigirlfriend.api.constant.MemoryConstant.*;
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

    private final MemoryClient memoryClient;

    private final RabbitTemplate rabbitTemplate;
    /**
     * 获取用户记忆
     */
    public String getMemory(){
        String ans = "";
        Long userId = getUserId();
        if(userId == null){
            return "未登录！";
        }
        UserContext.setUser(userId);
        Result<List<MemoryQuery>> result = memoryClient.queryMemoryList();
        if(result== null){
            return "";
        }
        List<MemoryQuery> data = result.getData();
        if(data == null || data.isEmpty()){
            return "";
        }
        for (MemoryQuery memoryQuery : data) {
            if(memoryQuery.getMemoryKey().equals(HABIT_KEY)){
                ans += "用户兴趣" + memoryQuery.getMemoryValue() + "用户认为这个模块的重要程度（最高5）" + memoryQuery.getImportance();
            }
            if(memoryQuery.getMemoryKey().equals(USER_INFO_KEY)){
                ans += "用户信息" + memoryQuery.getMemoryValue() + "用户认为这个模块的重要程度（最高5）" + memoryQuery.getImportance();
            }
            if(memoryQuery.getMemoryKey().equals(USER_NICKNAME_KEY)){
                ans += "用户希望你叫他的昵称" + memoryQuery.getMemoryValue() + "用户认为这个模块的重要程度（最高5）" + memoryQuery.getImportance();
            }
        }

        return ans;
    }

    /**
     * 获取角色
     * @return
     */
    public String getSystem(){
        Long userId = getUserId();
        if(userId == null){
            return "获取失败！";
        }
        UserContext.setUser(userId);
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
        if(title.length() > 15){
            return title.substring(0,14);
        }
        return title;
    }

    /**
     * 获取用户id
     * @return
     */
    public Long getUserId(){
        return UserContext.getUser();
    }
    /**
     * 发送消息
     *
     * @param userMessage
     * @param sessionId
     * @return
     */
    @Override
    public String sendMsg(String userMessage, Long sessionId) {
        Long curId = sessionId;
        if(sessionId == null){
            ChatSessionDTO chatSessionDTO = setChatSessionDTO(userMessage);
//            Result<Long> longResult = chatClients.saveSession(chatSessionDTO);
//            if(longResult != null){
//                curId = longResult.getData();
//            }
            try {
                Object response = rabbitTemplate.convertSendAndReceive("chat.exchange", "session", chatSessionDTO);
                if(response instanceof Long){
                    curId = (Long) response;
                }
            } catch (AmqpException e) {
                return "消息发送失败";
            }
        }
        String system = getSystem();
        String memory = getMemory();
        if(memory != null){
            system += memory;
        }
        String aiMessage = callDS(system, userMessage);
        if(!aiMessage.equals(ERROR_RETURN)){
            //发送消息成功，保存用户消息
            ChatMessagesDTO chatMessagesDTO = setChatMessageDTO(userMessage, USER_MSG,curId);
            rabbitTemplate.convertAndSend("chat.exchange","message",chatMessagesDTO);
            //chatClients.saveMessage(chatMessagesDTO);
            //保存ai消息
//            ThreadUtil.sleep(1000);
            ChatMessagesDTO aiMessageDTO = setChatMessageDTO(aiMessage, AI_MSG,curId);
            rabbitTemplate.convertAndSend("chat.exchange","message",aiMessageDTO);
//            chatClients.saveMessage(aiMessageDTO);
        }else{
            return "error";
        }
        return aiMessage;
    }
}

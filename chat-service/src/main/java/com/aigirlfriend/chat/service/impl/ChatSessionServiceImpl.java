package com.aigirlfriend.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.aigirlfriend.api.domain.dto.ChatSessionDTO;
import com.aigirlfriend.chat.domain.po.ChatMessages;
import com.aigirlfriend.chat.domain.po.ChatSession;
import com.aigirlfriend.chat.domain.vo.ChatSessionVO;
import com.aigirlfriend.chat.mapper.ChatSessionMapper;
import com.aigirlfriend.chat.service.IChatSessionService;
import com.aigirlfriend.commen.utils.Result;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatSessionServiceImpl extends ServiceImpl<ChatSessionMapper, ChatSession> implements IChatSessionService {

    /**
     * 新增会话
     * @param chatSessionDTO
     * @return
     */
    @Override
    public Result<Long> saveSession(ChatSessionDTO chatSessionDTO) {
        String sessionId = UUID.randomUUID().toString();
        Long userId = 1L;//TODO Context.getUser();
        if(userId == null){
            return Result.error("用户未登录！");
        }

        if(lambdaQuery().eq(ChatSession::getUserId,userId).eq(ChatSession::getSessionId,sessionId).exists()){
            return Result.error("创建会话失败！请稍后重试！");
        }
        ChatSession chatSession = BeanUtil.copyProperties(chatSessionDTO, ChatSession.class);
        chatSession.setUserId(userId);
        chatSession.setSessionId(sessionId);
        chatSession.setCreatedAt(LocalDateTime.now());
        chatSession.setUpdatedAt(LocalDateTime.now());
        chatSession.setStatus(true);
        save(chatSession);

        ChatSession one = lambdaQuery().eq(ChatSession::getUserId, userId).eq(ChatSession::getSessionId, sessionId).one();
        return Result.ok(one.getId());
    }

    /**
     * 根据id删除会话
     * @param id
     * @return
     */
    @Override
    public Result deleteSession(Long id) {
        Long userId = 1L;//TODO Context.getUser();
        if(userId == null){
            return Result.error("用户未登录！");
        }

        //删除当前会话
        ChatSession chatSession = lambdaQuery().eq(ChatSession::getId,id)
                .eq(ChatSession::getUserId,userId)
                .one();
        chatSession.setStatus(false);
        updateById(chatSession);

        //删除会话下的聊天消息
        List<ChatMessages> chatMessages = Db.lambdaQuery(ChatMessages.class)
                .eq(ChatMessages::getSessionId, id)
                .list();
        if(chatMessages == null || chatMessages.isEmpty()){
            return Result.ok();
        }
        for (ChatMessages chatMessage : chatMessages) {
            chatMessage.setDeleted(true);
        }
        Db.updateBatchById(chatMessages);

        return Result.ok();
    }

    /**
     * 查询会话列表
     * @return
     */
    @Override
    public Result<List<ChatSessionVO>> querySessionList() {
        Long userId = 1L;//TODO Context.getUser();
        if(userId == null){
            return Result.error("用户未登录！");
        }

        List<ChatSession> chatSessions = lambdaQuery().eq(ChatSession::getUserId, userId).eq(ChatSession::getStatus,true).list();
        if(chatSessions == null){
            return Result.ok(Collections.emptyList());
        }

        List<ChatSessionVO> chatSessionVOS = BeanUtil.copyToList(chatSessions, ChatSessionVO.class);

        return Result.ok(chatSessionVOS);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Result<ChatSessionVO> queryById(Long id) {
        Long userId = 1L;//TODO Context.getUser();
        if(userId == null){
            return Result.error("用户未登录！");
        }

        ChatSession chatSession = lambdaQuery().eq(ChatSession::getUserId,userId).eq(ChatSession::getId,id).one();
        if(chatSession == null){
            return Result.error("会话在当前用户下不存在！");
        }
        ChatSessionVO chatSessionVO = BeanUtil.copyProperties(chatSession, ChatSessionVO.class);
        return Result.ok(chatSessionVO);
    }


}

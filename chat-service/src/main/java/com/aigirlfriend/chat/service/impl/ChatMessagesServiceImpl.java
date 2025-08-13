package com.aigirlfriend.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aigirlfriend.api.domain.dto.ChatMessagesDTO;
import com.aigirlfriend.chat.constant.MessagesConstant;
import com.aigirlfriend.chat.domain.po.ChatMessages;
import com.aigirlfriend.chat.domain.vo.ChatMessagesVO;
import com.aigirlfriend.chat.mapper.ChatMessagesMapper;
import com.aigirlfriend.chat.service.IChatMessagesService;
import com.aigirlfriend.commen.utils.Result;
import com.aigirlfriend.commen.utils.UserContext;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessagesServiceImpl extends ServiceImpl<ChatMessagesMapper, ChatMessages> implements IChatMessagesService {

    private final ChatMessagesMapper chatMessagesMapper;

    /**
     * 新增消息
     * @param chatMessagesDTO
     * @return
     */
    @Override
    public Result saveMessage(ChatMessagesDTO chatMessagesDTO) {
        Long userId = UserContext.getUser();
        if(userId == null){
            return Result.error("用户不存在！");
        }

        if(chatMessagesDTO == null){
            return Result.error("消息获取错误！！！");
        }

        ChatMessages chatMessages = BeanUtil.copyProperties(chatMessagesDTO, ChatMessages.class);
        chatMessages.setUserId(userId);
        chatMessages.setSentAt(LocalDateTime.now());
        chatMessages.setDeleted(false);
        save(chatMessages);
        return Result.ok();
    }

    /**
     * 根据会话id查询消息
     * @param id
     * @return
     */

    @Override
    public Result<List<ChatMessagesVO>> queryMessages(Long id) {
        Long userId = UserContext.getUser();
        if(userId == null){
            return Result.error("用户未登录！");
        }
        if(id == null){
            return Result.error("会话不存在！");
        }

        List<ChatMessages> chatMessages = lambdaQuery().eq(ChatMessages::getSessionId, id).
                eq(ChatMessages::getUserId, userId)
                .eq(ChatMessages::getDeleted,false)
                .list();

        if(chatMessages == null){
            return Result.ok(Collections.emptyList());
        }

        List<ChatMessagesVO> chatMessagesVOS = BeanUtil.copyToList(chatMessages, ChatMessagesVO.class);

        return Result.ok(chatMessagesVOS);
    }

    /**
     * 删除消息
     * @param id
     * @return
     */
    @Override
    public Result deleteMessage(Long id) {
        if(id == null){
            return Result.error("明日方舟，启动！");
        }

        Long userId = UserContext.getUser();
        if(userId == null){
            return Result.error("用户未登录！");
        }

        ChatMessages chatMessage = getById(id);
        if(chatMessage == null){
            return Result.error("没有要删除的消息！");
        }
        if(!userId.equals(chatMessage.getUserId())){
            return Result.error("没有删除此消息的权限！");
        }

        chatMessage.setDeleted(MessagesConstant.IS_DELETED);
        chatMessage.setDeletedAt(LocalDateTime.now());
        updateById(chatMessage);
        return Result.ok();
    }
}

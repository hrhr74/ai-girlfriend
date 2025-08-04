package com.aigirlfriend.chat.service;

import com.aigirlfriend.api.domain.dto.ChatSessionDTO;
import com.aigirlfriend.chat.domain.po.ChatSession;
import com.aigirlfriend.chat.domain.vo.ChatSessionVO;
import com.aigirlfriend.commen.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IChatSessionService extends IService<ChatSession> {

    Result<Long> saveSession(ChatSessionDTO chatSessionDTO);

    Result deleteSession(Long id);

    Result<List<ChatSessionVO>> querySessionList();

    Result<ChatSessionVO> queryById(Long id);
}

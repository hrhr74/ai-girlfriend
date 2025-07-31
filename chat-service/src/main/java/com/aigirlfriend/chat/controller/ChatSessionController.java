package com.aigirlfriend.chat.controller;

import com.aigirlfriend.chat.domain.dto.ChatSessionDTO;
import com.aigirlfriend.chat.domain.vo.ChatSessionVO;
import com.aigirlfriend.chat.service.IChatSessionService;
import com.aigirlfriend.commen.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class ChatSessionController {
    private final IChatSessionService chatSessionService;

    /**
     * 新增会话
     */
    @PostMapping
    public Result saveSession(@RequestBody() ChatSessionDTO chatSessionDTO){
        return chatSessionService.saveSession(chatSessionDTO);
    }

    /**
     * 删除会话
     */
    @DeleteMapping("{id}")
    public Result deleteSession(@PathVariable("id") Long id){
        return chatSessionService.deleteSession(id);
    }
    /**
     * 查询会话列表
     */
    @GetMapping("list")
    public Result<List<ChatSessionVO>> querySessionList(){
        return chatSessionService.querySessionList();
    }
    /**
     * 查询单个会话
     */
    @PostMapping("{id}")
    public Result<ChatSessionVO> queryById(@PathVariable("id") Long id){
        return chatSessionService.queryById(id);
    }

}

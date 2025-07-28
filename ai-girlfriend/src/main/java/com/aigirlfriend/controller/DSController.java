package com.aigirlfriend.controller;

import com.aigirlfriend.service.IDeepSeekService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("DS")
@RequestMapping("/ai")
@Slf4j
@RequiredArgsConstructor
public class DSController {


    private final IDeepSeekService deepSeekService;

    @PostMapping("/chat")
    public String chat(@RequestParam String userMessage){
        log.debug("aaa");
        return deepSeekService.callDS(userMessage);
    }
}

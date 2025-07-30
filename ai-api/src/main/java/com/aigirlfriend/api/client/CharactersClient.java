package com.aigirlfriend.api.client;

import com.aigirlfriend.api.domain.vo.AiCharactersVO;
import com.aigirlfriend.commen.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("character-service")
public interface CharactersClient{
    @PostMapping("/character/default")
    //获取默认角色
    public Result<AiCharactersVO> getDefault();
}

package com.aigirlfriend.api.client;

import com.aigirlfriend.commen.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.aigirlfriend.api.domain.query.MemoryQuery;
import java.util.List;

@FeignClient("memory-service")
public interface MemoryClient {
    @GetMapping("/memory/list")
    Result<List<MemoryQuery>> queryMemoryList();
}

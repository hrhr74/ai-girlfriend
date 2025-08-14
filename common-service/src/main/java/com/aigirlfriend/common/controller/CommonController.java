package com.aigirlfriend.common.controller;

import cn.hutool.core.lang.UUID;
import com.aigirlfriend.common.utils.AliOssUtil;
import com.aigirlfriend.commen.utils.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
@Slf4j
public class CommonController {
    private final AliOssUtil aliOssUtil;
    /**
     * 文件上传
     */
    @PostMapping("upload")
    public Result upload(MultipartFile file){
        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //获取后缀名
            String substring = originalFilename.substring(originalFilename.indexOf("."));
            //新文件名
            String fileName = UUID.randomUUID().toString() + substring;
            String url = aliOssUtil.upload(file.getBytes(), fileName);

            return Result.ok(url);
        }catch (IOException e){
            log.error("文件上传失败");
            return Result.error("文件上传失败");
        }
    }
}

package com.aigirlfriend.character.controller;

import cn.hutool.core.bean.BeanUtil;
import com.aigirlfriend.character.domain.dto.AiCharactersDTO;
import com.aigirlfriend.character.domain.vo.AiCharactersVO;
import com.aigirlfriend.character.service.IAiCharactersService;
import com.aigirlfriend.commen.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/character")
@RequiredArgsConstructor
public class AiCharactersController {
    private final IAiCharactersService aiCharactersService;
    /**
     * 新增女友角色
     */
    @PostMapping
    public Result addCharacter(@RequestBody AiCharactersDTO aiCharactersDTO){
        return aiCharactersService.addCharacter(aiCharactersDTO);
    }
    /**
     * 查看用户的女友角色
     */
    @GetMapping("list")
    public Result<List<AiCharactersVO>> listCharacters(){
        return aiCharactersService.listAiCharacters();
    }
    /**
     * 修改女友角色
     */
    @PutMapping
    public Result updateCharacter(@RequestBody AiCharactersDTO aiCharactersDTO){
        return aiCharactersService.updateCharacter(aiCharactersDTO);
    }
    /**
     * 根据id查询角色
     */
    @GetMapping("{id}")
    public Result<AiCharactersVO> getCharacterById(@PathVariable("id") Long id){
        return Result.ok(BeanUtil.copyProperties(aiCharactersService.getById(id), AiCharactersVO.class));
    }
}

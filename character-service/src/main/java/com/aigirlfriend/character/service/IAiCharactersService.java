package com.aigirlfriend.character.service;

import com.aigirlfriend.character.domain.dto.AiCharactersDTO;
import com.aigirlfriend.character.domain.po.AiCharacters;
import com.aigirlfriend.character.domain.vo.AiCharactersVO;
import com.aigirlfriend.commen.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface IAiCharactersService extends IService<AiCharacters> {

    Result addCharacter(AiCharactersDTO aiCharactersDTO);

    Result<List<AiCharactersVO>> listAiCharacters();

    Result updateCharacter(AiCharactersDTO aiCharactersDTO);
}

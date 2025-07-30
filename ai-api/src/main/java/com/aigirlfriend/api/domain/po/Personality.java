package com.aigirlfriend.api.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Personality {
    // 核心性格类型
    private String corePersonality;

    // 性格特质列表
    private List<String> traits;

    // 语言风格
    private String speechStyle;

    // 回应倾向
    private Integer empathyLevel;    // 共情程度(1-10)
    private Integer humorLevel;      // 幽默程度(1-10)
    private Integer directnessLevel; // 直接程度(1-10)
    private String emojiUsage;       // 表情使用频率(none/rare/medium/frequent)

    // 兴趣爱好
    private List<String> interests;

    // 特殊特征
    private List<String> catchphrases; // 口头禅
    private String signatureEmoji;     // 标志性表情
    private Map<String, String> characteristicResponses; // 特征回应
}

package com.aigirlfriend.user.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO {
    private Long id;

    private String username;

    //jwt校验令牌
    private String token;
}

package com.aigirlfriend.user.domain.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String phone;
    private String avatar;
}

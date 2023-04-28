package com.sparta.Myposts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LoginRequestDto {

    private String username;
    private String password;
}
package com.sparta.Myposts.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SignupRequestDto {

    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    private String username;
    @Pattern(regexp = "^[a-zA-Z0-9@$!%*#?&]{8,15}$")
    private String password;
    private boolean admin = false;
    private String adminToken = "";
}

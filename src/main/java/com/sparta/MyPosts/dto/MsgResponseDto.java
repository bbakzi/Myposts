package com.sparta.MyPosts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class MsgResponseDto {
    private String msg;
    private HttpStatus statusCode;
}

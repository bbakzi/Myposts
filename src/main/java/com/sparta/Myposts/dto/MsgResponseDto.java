package com.sparta.Myposts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class MsgResponseDto {
    private String msg;
    private HttpStatus statusCode;
    //private HttpStatusCode statusCode;

    public MsgResponseDto(String msg, HttpStatus statusCode){
        this.msg = msg;
        this.statusCode = statusCode;
    }
}

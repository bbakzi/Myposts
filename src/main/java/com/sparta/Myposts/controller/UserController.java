package com.sparta.Myposts.controller;

import com.sparta.Myposts.dto.LoginRequestDto;
import com.sparta.Myposts.dto.MsgResponseDto;
import com.sparta.Myposts.dto.SignupRequestDto;
import com.sparta.Myposts.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
//@RequestMapping ("/") <- 이곳으로 찾아가세요~ 하는 @
public class UserController {

    private final UserService userService;

    //signup
    @PostMapping("/signup")
    public MsgResponseDto signup (@RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    @ResponseBody
    @PostMapping("/login")
    public MsgResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return userService.login(loginRequestDto, response);
    }
}

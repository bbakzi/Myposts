package com.sparta.MyPosts.controller;

import com.sparta.MyPosts.dto.LoginRequestDto;
import com.sparta.MyPosts.dto.MsgResponseDto;
import com.sparta.MyPosts.dto.SignupRequestDto;
import com.sparta.MyPosts.service.UserService;
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

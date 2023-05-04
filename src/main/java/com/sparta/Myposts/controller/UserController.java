package com.sparta.Myposts.controller;

import com.sparta.Myposts.dto.LoginRequestDto;
import com.sparta.Myposts.dto.MsgResponseDto;
import com.sparta.Myposts.dto.SignupRequestDto;
import com.sparta.Myposts.security.UserDetailsImpl;
import com.sparta.Myposts.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
//@RequestMapping ("/") <- 이곳으로 찾아가세요~ 하는 @
public class UserController {

    private final UserService userService;

    //signup
    @PostMapping("/signup")
    public MsgResponseDto signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    @PostMapping("/login")
    public MsgResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return userService.login(loginRequestDto, response);
    }

//    @GetMapping("/forbidden")
//    public ModelAndView getForbidden() {
//        return new ModelAndView("forbidden");
//    }
//    @PostMapping("/forbidden")
//    public ModelAndView postForbidden() {
//        return new ModelAndView("forbidden");
//    }
}

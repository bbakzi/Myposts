package com.sparta.MyPosts.service;

import com.sparta.MyPosts.dto.LoginRequestDto;
import com.sparta.MyPosts.dto.MsgResponseDto;
import com.sparta.MyPosts.dto.SignupRequestDto;
import com.sparta.MyPosts.entity.User;
import com.sparta.MyPosts.jwt.JwtUtil;
import com.sparta.MyPosts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor

public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    //회원가입
    @Transactional //sign up
    public MsgResponseDto signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        //중복확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            return new MsgResponseDto("중복된 사용자가 존재합니다.", HttpStatus.BAD_REQUEST);
        }
        if (!Pattern.matches("^[a-z0-9]{4,10}$", username) || !Pattern.matches("^[a-zA-Z0-9]{8,15}$", password)) {
            return new MsgResponseDto("회원가입 양식 조건에 맞지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        User user = new User(username, password);
        userRepository.save(user);
        return new MsgResponseDto("회원가입 성공", HttpStatus.OK);
    }

    @Transactional
    public MsgResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        //비밀번호 확인
        if(!user.getPassword().equals(password)) {
            return new MsgResponseDto("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));
        return new MsgResponseDto("로그인 성공", HttpStatus.OK);
    }
}

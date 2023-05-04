package com.sparta.Myposts.service;

import com.sparta.Myposts.dto.LoginRequestDto;
import com.sparta.Myposts.dto.MsgResponseDto;
import com.sparta.Myposts.dto.SignupRequestDto;
import com.sparta.Myposts.entity.User;
import com.sparta.Myposts.entity.UserRoleEnum;
import com.sparta.Myposts.jwt.JwtUtil;
import com.sparta.Myposts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    // ADMIN_TOKEN
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    //회원가입
    @Transactional //sign up
    public MsgResponseDto signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        //중복확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            return new MsgResponseDto("중복된 Username 입니다.", HttpStatus.BAD_REQUEST);
        }
//        if (!Pattern.matches("^[a-z0-9]{4,10}$", username) || !Pattern.matches("^[a-zA-Z0-9\\d@$!%*#?&]{8,15}$", password)) {
//            //정규식으로 특수 문자 넣기 java는 \d 아니고 \\, \는 특수문자로 인식하기 때문 ^[a-zA-Z\p{Punct}0-9]*$ ->함수
//            return new MsgResponseDto("회원가입 양식 조건에 맞지 않습니다.", HttpStatus.BAD_REQUEST);
        // 사용자 ROLE(권한) 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        User user = new User(username, password, role);
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
        if(!passwordEncoder.matches(password, user.getPassword())) {
            return new MsgResponseDto("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(),user.getRole()));
        return new MsgResponseDto("로그인 성공", HttpStatus.OK);
    }
}

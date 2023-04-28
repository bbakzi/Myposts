package com.sparta.Myposts.service;

import com.sparta.Myposts.dto.*;
import com.sparta.Myposts.entity.Comment;
import com.sparta.Myposts.entity.Post;
import com.sparta.Myposts.entity.User;
import com.sparta.Myposts.entity.UserRoleEnum;
import com.sparta.Myposts.jwt.JwtUtil;
import com.sparta.Myposts.repository.CommentRepository;
import com.sparta.Myposts.repository.PostRepository;
import com.sparta.Myposts.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;



@Service
@RequiredArgsConstructor
public class CommentService {
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String SUBJECT_KEY = "sub";
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PostRepository postRepository;

    //comment 작성
    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        // 토큰이 있는 경우에만 comment 추가 가능
        if (token == null) {
            return null;
        }
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
            // 토큰에서 사용자 정보 가져오기
        }
        claims = jwtUtil.getUserInfoFromToken(token);
        // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );
        Comment comment = new Comment(commentRequestDto, user.getUsername(), post);
        commentRepository.saveAndFlush(comment);
        return new CommentResponseDto(comment);

    }

    //comment 수정
    @Transactional
    public CommentResponseDto updateComment(Long Id, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        Comment comment = commentRepository.findById(Id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        // 토큰이 있는 경우에만 관심상품 최저가 업데이트 가능
        if (token == null) {
            return null;
        }
        // Token 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
        // 토큰에서 사용자 정보 가져오기
        claims = jwtUtil.getUserInfoFromToken(token);
        // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
        String username = claims.get(SUBJECT_KEY, String.class);
        String role = claims.get(AUTHORIZATION_KEY, String.class);

        if (StringUtils.equals(role, UserRoleEnum.USER.name())) {
            //너는 이 글을 작성한 사람이니?
            if (!StringUtils.equals(comment.getUsername(), username)) {
                throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
            } else {
                comment.updateComment(commentRequestDto);
                return new CommentResponseDto(comment);
            }
        }
        comment.updateComment(commentRequestDto);
        return new CommentResponseDto(comment);
    }

    //comment 삭제
    @Transactional
    public MsgResponseDto delete(Long Id, HttpServletRequest request) {
        Comment comment = commentRepository.findById(Id).orElseThrow(
                () -> new NullPointerException("존재하지 않는 게시글입니다.")
        );
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token == null) {
            return null;
        }
        // Token 검증
        if (!jwtUtil.validateToken(token)) {
            return new MsgResponseDto("토큰이 유효하지 않습니다.", HttpStatus.valueOf(400));
        }
        claims = jwtUtil.getUserInfoFromToken(token);

        String username = claims.get(SUBJECT_KEY, String.class);
        String role = claims.get(AUTHORIZATION_KEY, String.class);
        //너의 역할을 알려줘~
        if (StringUtils.equals(role, UserRoleEnum.USER.name())) {
            //너는 이 글을 작성한 사람이니?
            if (!StringUtils.equals(comment.getUsername(), username)) {
                    return new MsgResponseDto("ID가 같지 않습니다.", HttpStatus.valueOf(400));
                } else {
                commentRepository.delete(comment);
                return new MsgResponseDto("삭제완료", HttpStatus.valueOf(200));
            }
        }
        commentRepository.delete(comment);
        return new MsgResponseDto("삭제완료", HttpStatus.valueOf(200));
    }
}
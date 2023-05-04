package com.sparta.Myposts.service;

import com.sparta.Myposts.dto.*;
import com.sparta.Myposts.entity.UserRoleEnum;
import com.sparta.Myposts.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.thymeleaf.util.StringUtils;
import com.sparta.Myposts.entity.Post;
import com.sparta.Myposts.entity.User;
import com.sparta.Myposts.jwt.JwtUtil;
import com.sparta.Myposts.repository.PostRepository;
import com.sparta.Myposts.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    //UserRoleEnum을 확인
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String SUBJECT_KEY = "sub";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private  final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;


    //게시물 전체조회
    @Transactional(readOnly = true)
    public List<AllResponseDto> getAllPosts() {
        List<AllResponseDto> allResponseDto = new ArrayList<>();
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();

        for(Post post : posts ) {
            List<CommentResponseDto> commentResponseDto = new ArrayList<>();
            commentResponseDto = commentRepository.findAllComment(post.getId());
            allResponseDto.add(new AllResponseDto(post, commentResponseDto));
        }
        return allResponseDto;
            //테이블에 저장되어있는 모든 게시글 조회
            //뽑아온 데이터를 map을 통해 responsedto로 가공, collect가 list 타입으로 묶어줌.
    }

    //게시물 작성
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, HttpServletRequest request, User user) {
        //httpRequest 토큰 가지고 오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token == null) {
            return null;
        }
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
            // 토큰에서 사용자 정보 가져오기
        }
        claims = jwtUtil.getUserInfoFromToken(token);

        // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
        user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );

        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Post post = postRepository.saveAndFlush(new Post(postRequestDto, user.getUsername()));

        return new PostResponseDto(post);

    }

    //선택 게시글 조회
    @Transactional(readOnly = true)
    public AllResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        List<CommentResponseDto> comments = commentRepository.findAllComment(id);

        return new AllResponseDto(post,comments);
    }

    //게시글 수정
    @Transactional
    public PostResponseDto update
    (Long id, PostRequestDto postRequestDto, HttpServletRequest request) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
        String token = jwtUtil.resolveToken(request);
        Claims claims;

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

        //너의 역할을 알려줘~
        if (StringUtils.equals(role, UserRoleEnum.USER.name())) {
            //너는 이 글을 작성한 사람이니?
            if (!StringUtils.equals(post.getUsername(), username)) {
                throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
            } else {
                post.update(postRequestDto);
                return new PostResponseDto(post);
            }
        }
        post.update(postRequestDto);
        return new PostResponseDto(post);
    }


    //게시글 삭제
    @Transactional
    public MsgResponseDto delete(Long id, HttpServletRequest request) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token == null) {
            return null;
        }
        // Token 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
        claims = jwtUtil.getUserInfoFromToken(token);

        String username = claims.get(SUBJECT_KEY, String.class);
        String role = claims.get(AUTHORIZATION_KEY, String.class);

        //너의 역할을 알려줘~
        if (StringUtils.equals(role, UserRoleEnum.USER.name())) {
            //너는 이 글을 작성한 사람이니?
            if (!StringUtils.equals(post.getUsername(), username)) {
                throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
            } else {
                commentRepository.deleteCommentByPost_Id(post.getId());
                postRepository.delete(post);
                return new MsgResponseDto("삭제완료", HttpStatus.OK);
            }
        }
        commentRepository.deleteCommentByPost_Id(post.getId());
        postRepository.delete(post);
        return new MsgResponseDto("삭제완료", HttpStatus.OK);
    }

    }


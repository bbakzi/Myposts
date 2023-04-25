package com.sparta.MyPosts.service;

import com.sparta.MyPosts.dto.MsgResponseDto;
import org.springframework.http.HttpStatus;
import org.thymeleaf.util.StringUtils;
import com.sparta.MyPosts.dto.PostRequestDto;
import com.sparta.MyPosts.dto.PostResponseDto;
import com.sparta.MyPosts.entity.Post;
import com.sparta.MyPosts.entity.User;
import com.sparta.MyPosts.jwt.JwtUtil;
import com.sparta.MyPosts.repository.PostRepository;
import com.sparta.MyPosts.repository.UserRepository;
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

    public static final String SUBJECT_KEY = "sub";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    //게시물 전체조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();
        {
            return posts.stream().map(post -> new PostResponseDto(post)).toList();
            //테이블에 저장되어있는 모든 게시글 조회
            //뽑아온 데이터를 map을 통해 responsedto로 가공, collect가 list 타입으로 묶어줌.
        }
    }

    //게시물 작성
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, HttpServletRequest request) {
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
        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );

        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Post post = postRepository.saveAndFlush(new Post(postRequestDto, user.getUsername()));

        return new PostResponseDto(post);

    }

    //게시글 한개 조회
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        return new PostResponseDto(post);
    }

    //게시글 수정
    @Transactional
    public PostResponseDto update(Long id, PostRequestDto postRequestDto, HttpServletRequest request) {
        Post post = postRepository.findById(id).orElseThrow(
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
            throw new IllegalArgumentException("Token Error");
        }
        // 토큰에서 사용자 정보 가져오기
        claims = jwtUtil.getUserInfoFromToken(token);

        // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
        String username = claims.get(SUBJECT_KEY, String.class);

        if (!StringUtils.equals(post.getUsername(), username)) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }
        post.update(postRequestDto);
        return new PostResponseDto(post);
    }


    //게시글 삭제
    @Transactional
    public MsgResponseDto delete(Long id, HttpServletRequest request) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않는 게시글입니다.")
        );

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token == null) {
            return null;
        }
        // Token 검증
        if (!jwtUtil.validateToken(token)) {
            return new MsgResponseDto("token error", HttpStatus.BAD_REQUEST);
        }
        claims = jwtUtil.getUserInfoFromToken(token);

        String username = claims.get(SUBJECT_KEY, String.class);
        if (!StringUtils.equals(post.getUsername(), username)) {
            return new MsgResponseDto("ID가 같지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        postRepository.delete(post);
        return new MsgResponseDto("삭제완료",HttpStatus.OK);
    }

}



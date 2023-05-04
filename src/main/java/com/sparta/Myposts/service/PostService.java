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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;


    //게시물 전체조회
    @Transactional(readOnly = true)
    public List<AllResponseDto> getAllPosts() {
        List<AllResponseDto> allResponseDto = new ArrayList<>();
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();

        for (Post post : posts) {
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
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
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

        return new AllResponseDto(post, comments);
    }

    //게시글 수정
    @Transactional
    public PostResponseDto update(Long id, PostRequestDto postRequestDto, User user) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않는 게시글입니다.")
        );

        UserRoleEnum userRoleEnum = user.getRole();

        // RoleEnum과 username을 비교
        // StringUtils 찾아보기
        if (StringUtils.equals(userRoleEnum, UserRoleEnum.USER.name())) {
            if (!StringUtils.equals(post.getUsername(), user.getUsername())) {
                throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
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
    public MsgResponseDto deleteAll(Long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("게시글이 존재하지 않습니다.")
        );

        UserRoleEnum userRoleEnum = user.getRole();

        if (StringUtils.equals(userRoleEnum, UserRoleEnum.USER.name())) {
            if (!StringUtils.equals(post.getUsername(), user.getUsername())) {
                return new MsgResponseDto("아이디가 같지 않습니다.", HttpStatus.BAD_REQUEST);
            } else {
                commentRepository.deleteCommentByPost_Id(post.getId()); // 게시물 삭제시 댓글 같이 삭제
                postRepository.delete(post);
                return new MsgResponseDto("삭제 성공", HttpStatus.OK);
            }
        }
        commentRepository.deleteCommentByPost_Id(post.getId()); // 게시물 삭제시 댓글 같이 삭제
        postRepository.delete(post);
        return new MsgResponseDto("삭제 성공", HttpStatus.OK);
    }
}


package com.sparta.Myposts.controller;

import com.sparta.Myposts.dto.AllResponseDto;
import com.sparta.Myposts.dto.MsgResponseDto;
import com.sparta.Myposts.dto.PostRequestDto;
import com.sparta.Myposts.dto.PostResponseDto;
import com.sparta.Myposts.entity.UserRoleEnum;
import com.sparta.Myposts.security.UserDetailsImpl;
import com.sparta.Myposts.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
//Spring MVC의 @RestController은 @Controller와 @ResponseBody의 조합.
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //게시글 전체조회 구현 완료!
    @GetMapping("/get")
    public List<AllResponseDto> getPosts(){
        return postService.getAllPosts();
    }

    //게시글 입력
    //@Secured(UserRoleEnum.Authority.ADMIN)
    @PostMapping("/post")
    public PostResponseDto createPost (@RequestBody PostRequestDto postRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.createPost(postRequestDto,userDetails.getUser());
    }

    //게시글 하나 조회
    @GetMapping ("/get/{id}") //작성 날짜 작성 내용 조회
    public AllResponseDto getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    //게시글 수정
    @PutMapping("/put/{id}")
    //@PathVariable 이것도 정리해서 til 작성하고 주석에 최대한 짧게 정리
    public PostResponseDto update(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.update(id,postRequestDto,userDetails.getUser());
    }

    //게시글 삭제
    @DeleteMapping("/delete/{id}")
    public MsgResponseDto delete(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.deleteAll(id,userDetails.getUser());
    }
}

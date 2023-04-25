package com.sparta.MyPosts.controller;

import com.sparta.MyPosts.dto.MsgResponseDto;
import com.sparta.MyPosts.dto.PostRequestDto;
import com.sparta.MyPosts.dto.PostResponseDto;
import com.sparta.MyPosts.service.PostService;
import lombok.RequiredArgsConstructor;
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
    public List<PostResponseDto> getPosts(){
        return postService.getAllPosts();
    }

    //게시글 입력
    @PostMapping("/post")
    public PostResponseDto createPost (@RequestBody PostRequestDto postRequestDto, HttpServletRequest request){
        return postService.createPost(postRequestDto,request);
    }

    //게시글 하나 조회
    @GetMapping ("/get/{id}") //작성 날짜 작성 내용 조회
    public PostResponseDto getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    //게시글 수정
    @PutMapping("/put/{id}")
    //@PathVariable 이것도 정리해서 til 작성하고 주석에 최대한 짧게 정리
    public PostResponseDto update(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, HttpServletRequest request){
        return postService.update(id,postRequestDto,request);
    }

    //게시글 삭제
    @DeleteMapping("/delete/{id}")
    public MsgResponseDto delete(@PathVariable Long id, HttpServletRequest request){
        return postService.delete(id,request);
    }
}

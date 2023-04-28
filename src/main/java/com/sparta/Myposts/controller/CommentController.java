package com.sparta.Myposts.controller;

import com.sparta.Myposts.dto.CommentRequestDto;
import com.sparta.Myposts.dto.CommentResponseDto;
import com.sparta.Myposts.dto.MsgResponseDto;
import com.sparta.Myposts.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class CommentController {

    private final CommentService commentService;

    //comment 작성
    @PostMapping("/post/{id}/comment")
    //게시글이 있는지 확인
    public CommentResponseDto createComment
    (@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        return commentService.createComment(id, commentRequestDto, request);
    }

    //comment 수정
    @PutMapping("/put/comment/{id}")
    public CommentResponseDto updateComment
    (@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        return commentService.updateComment(id, commentRequestDto, request);
    }

    //comment 삭제 그럼 두개 이상 달린 댓글은 어떻게 구분을 하지..?
    @DeleteMapping("/delete/comment/{id}")
    public MsgResponseDto delete(@PathVariable Long id,HttpServletRequest request){
        return commentService.delete(id,request);
    }

}

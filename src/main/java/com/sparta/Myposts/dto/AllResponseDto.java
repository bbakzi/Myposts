package com.sparta.Myposts.dto;

import com.sparta.Myposts.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AllResponseDto {

    private Long id;
    private String username;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> comments;

    public AllResponseDto(Post post, List<CommentResponseDto> comments) {
        this.id= post.getId();
        this.username = post.getUsername();
        this.title = post.getTitle();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.comments = comments;
    }
}
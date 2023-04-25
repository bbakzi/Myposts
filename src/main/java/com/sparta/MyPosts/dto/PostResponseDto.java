package com.sparta.MyPosts.dto;

import com.sparta.MyPosts.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDto {

    private String username;

    private String title;

    private String contents;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;


    public PostResponseDto(Post post) {
        this.username = post.getUsername();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();

    }

}

package com.sparta.Myposts.entity;

import com.sparta.Myposts.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//.AUTO
    private Long id; //comment uid

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String username;

    @ManyToOne
    @JoinColumn(name="Post_id", nullable = false)
    private Post post;

    public Comment(CommentRequestDto commentResponseDto, String username, Post post) {
        this.comment = commentResponseDto.getComment();
        this.username=username;
        this.post = post;
    }

    public void updateComment(CommentRequestDto commentRequestDto){
        this.comment = commentRequestDto.getComment();
    }
}

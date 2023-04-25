package com.sparta.MyPosts.entity;

import com.sparta.MyPosts.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post extends com.sparta.MyPosts.entity.TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //자동으로 Id 값을 생성하여 반영한다.
    private Long id; //<-이 Id는 게시글의 Id 헷갈리지 말자.

    //@Column : 객체 필드를 테이블의 컬럼에 매핑시켜주는 어노테이션
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String username;

    //새로운 게시물 작성
    public Post (PostRequestDto postRequestDto,String username){
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
        this.username = username;

    }

    //기존 게시물 수정
    public void update(PostRequestDto postRequestDto){
        //왜 여기는 void 일까?
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();

    }

    //게시물 삭제
    public void delete(PostRequestDto postRequestDto){
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
    }
}

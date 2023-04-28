package com.sparta.Myposts.repository;

import com.sparta.Myposts.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByOrderByModifiedAtDesc();//내림차순 설정
//    @Query("select p from Post p order by p.modifiedAt desc")
//    List<Post>allPostList();

}

package com.sparta.MyPosts.repository;

import com.sparta.MyPosts.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository <Post,Long> {
    List<Post> findAllByOrderByModifiedAtDesc(); //내림차순 설정
    Optional<Post> findById (Long Id);
}

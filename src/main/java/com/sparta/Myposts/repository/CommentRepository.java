package com.sparta.Myposts.repository;

import com.sparta.Myposts.dto.CommentResponseDto;
import com.sparta.Myposts.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.post.id = :id order by c.modifiedAt desc")//내림차순 정렬 createdAt
    List<CommentResponseDto> findAllComment(@Param("id") Long id);

//    @Query("delete from Comment where Comment.post.id = :id")
    void deleteCommentByPost_Id(Long id);
}
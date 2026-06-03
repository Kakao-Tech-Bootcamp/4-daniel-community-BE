package com.daniel.community.comment.repository;

import com.daniel.community.comment.entity.Comment;
import com.daniel.community.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시글에 달린 댓글을 CommentId 기준 오름차순으로 반환
    List<Comment> findByPostOrderByCommentIdAsc(Post post);

    int countByPost(Post post);
}
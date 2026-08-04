package com.daniel.community.post.repository;

import com.daniel.community.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying(
            flushAutomatically = true,
            clearAutomatically = true
    )
    @Query("""
            update Post post
               set post.views = post.views + 1
             where post.postId = :postId
            """)
    int increaseViews(@Param("postId") Long postId);
}
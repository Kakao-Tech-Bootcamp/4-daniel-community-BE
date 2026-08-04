package com.daniel.community.post.repository;

import com.daniel.community.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository
        extends JpaRepository<Post, Long> {
}
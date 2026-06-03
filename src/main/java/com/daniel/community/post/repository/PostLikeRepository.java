package com.daniel.community.post.repository;

import com.daniel.community.post.entity.Post;
import com.daniel.community.post.entity.PostLike;
import com.daniel.community.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    // 해당 게시글에 해당 사용자의 좋아요가 있는지 확인
    boolean existsByPostAndUser(Post post, User user);

    // 해당 게시글에 해당 사용자가 누른 좋아요 조회
    Optional<PostLike> findByPostAndUser(Post post, User user);

    // 해당 게시글의 좋아요 개수 조회
    int countByPost(Post post);
}
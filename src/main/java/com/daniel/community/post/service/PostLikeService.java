package com.daniel.community.post.service;

import com.daniel.community.post.entity.Post;
import com.daniel.community.post.entity.PostLike;
import com.daniel.community.post.repository.PostLikeRepository;
import com.daniel.community.post.repository.PostRepository;
import com.daniel.community.user.entity.User;
import com.daniel.community.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostLikeService(
            PostLikeRepository postLikeRepository,
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void likePost(Long postId, Long userId) {
        // 존재하지 않는 게시글과 사용자는 기존과 동일하게 예외처리
        findPost(postId);
        findUser(userId);

        // 이미 좋아요가 존재하면 정상종료
        postLikeRepository.insertIfAbsent(postId, userId);
    }

    @Transactional
    public int unlikePost(Long postId, Long userId) {
        Post post = findPost(postId);
        User user = findUser(userId);

        // 좋아요 데이터를 조회
        PostLike postLike = postLikeRepository.findByPostAndUser(post, user)
                .orElse(null);

        if (postLike == null) {
            return postLikeRepository.countByPost(post);
        }

        // 좋아요 데이터가 있으면 삭제
        postLikeRepository.delete(postLike);

        return postLikeRepository.countByPost(post);
    }

    // 게시글이 없을 경우 예외처리
    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post_not_found"));
    }

    // 사용자가 유효하지 않을 때 예외처리
    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("unauthorized"));
    }
}
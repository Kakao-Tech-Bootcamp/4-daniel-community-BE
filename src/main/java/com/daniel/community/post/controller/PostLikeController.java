package com.daniel.community.post.controller;

import com.daniel.community.global.response.ApiResponse;
import com.daniel.community.global.security.CustomUserDetails;
import com.daniel.community.post.service.PostLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostLikeController {

    private final PostLikeService postLikeService;

    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<ApiResponse> likePost(
            @PathVariable Long postId,
            // 현재 로그인한 사용자 정보를 주입
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        postLikeService.likePost(postId, userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.success("like_success"));
    }

    @DeleteMapping("/posts/{postId}/likes")
    public ResponseEntity<ApiResponse> unlikePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        postLikeService.unlikePost(postId, userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.success("unlike_success"));
    }
}
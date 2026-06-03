package com.daniel.community.post.controller;

import com.daniel.community.global.response.ApiResponse;
import com.daniel.community.global.security.CustomUserDetails;
import com.daniel.community.post.service.PostLikeService;
import org.springframework.http.HttpStatus;
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

    // 좋아요 등록
    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<ApiResponse> likePost(
            @PathVariable Long postId,
            // 현재 로그인한 사용자 정보 주입
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        try {
            postLikeService.likePost(postId, userDetails.getUserId());

            return ResponseEntity.ok(ApiResponse.success("like_success"));
        } catch (IllegalArgumentException exception) {
            HttpStatus status = getLikeErrorStatus(exception.getMessage());

            return ResponseEntity
                    .status(status)
                    .body(ApiResponse.error(exception.getMessage()));
        }
    }

    // 좋아요 취소
    @DeleteMapping("/posts/{postId}/likes")
    public ResponseEntity<ApiResponse> unlikePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        try {
            postLikeService.unlikePost(postId, userDetails.getUserId());

            return ResponseEntity.ok(ApiResponse.success("unlike_success"));
        } catch (IllegalArgumentException exception) {
            HttpStatus status = getLikeErrorStatus(exception.getMessage());

            return ResponseEntity
                    .status(status)
                    .body(ApiResponse.error(exception.getMessage()));
        }
    }

    private HttpStatus getLikeErrorStatus(String message) {
        if ("unauthorized".equals(message)) {
            return HttpStatus.UNAUTHORIZED;
        }

        return HttpStatus.NOT_FOUND;
    }
}
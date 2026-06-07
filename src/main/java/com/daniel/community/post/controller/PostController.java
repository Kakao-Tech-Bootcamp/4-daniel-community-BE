package com.daniel.community.post.controller;

import com.daniel.community.global.response.ApiResponse;
import com.daniel.community.global.security.CustomUserDetails;
import com.daniel.community.post.dto.CreatePostRequest;
import com.daniel.community.post.dto.CreatePostResponse;
import com.daniel.community.post.dto.PostDetailResponse;
import com.daniel.community.post.dto.PostListResponse;
import com.daniel.community.post.dto.UpdatePostRequest;
import com.daniel.community.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createPost(
            @Valid @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CreatePostResponse response =
                postService.createPost(request, userDetails.getUserId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("create_post_success", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getPosts(@RequestParam(required = false) Long cursor) {
        PostListResponse response = postService.getPosts(cursor);

        return ResponseEntity.ok(ApiResponse.success("get_posts_success", response));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse> getPost(@PathVariable Long postId) {
        PostDetailResponse response = postService.getPost(postId);

        return ResponseEntity.ok(ApiResponse.success("get_post_detail_success", response));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        postService.updatePost(postId, request, userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.success("update_post_success"));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        postService.deletePost(postId, userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.success("delete_post_success"));
    }
}
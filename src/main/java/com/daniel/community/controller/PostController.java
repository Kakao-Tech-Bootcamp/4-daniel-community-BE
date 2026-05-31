package com.daniel.community.controller;

import com.daniel.community.dto.ApiResponse;
import com.daniel.community.dto.CreatePostRequest;
import com.daniel.community.dto.CreatePostResponse;
import com.daniel.community.dto.PostDetailResponse;
import com.daniel.community.dto.PostListResponse;
import com.daniel.community.dto.UpdatePostRequest;
import com.daniel.community.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
        CreatePostResponse response = postService.createPost(request);

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
        try {
            PostDetailResponse response = postService.getPost(postId);
            return ResponseEntity.ok(ApiResponse.success("get_post_detail_success", response));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(exception.getMessage()));
        }
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request
    ) {
        try {
            postService.updatePost(postId, request);
            return ResponseEntity.ok(ApiResponse.success("update_post_success"));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(exception.getMessage()));
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long postId) {
        try {
            postService.deletePost(postId);
            return ResponseEntity.ok(ApiResponse.success("delete_post_success"));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(exception.getMessage()));
        }
    }
}

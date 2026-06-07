package com.daniel.community.comment.controller;

import com.daniel.community.comment.dto.CommentResponse;
import com.daniel.community.comment.dto.CreateCommentRequest;
import com.daniel.community.comment.dto.CreateCommentResponse;
import com.daniel.community.comment.dto.UpdateCommentRequest;
import com.daniel.community.comment.service.CommentService;
import com.daniel.community.global.response.ApiResponse;
import com.daniel.community.global.security.CustomUserDetails;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse> getComments(@PathVariable Long postId) {
        List<CommentResponse> response = commentService.getComments(postId);

        return ResponseEntity.ok(
                ApiResponse.success("get_comments_success", response)
        );
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CreateCommentResponse response =
                commentService.createComment(postId, request, userDetails.getUserId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("create_comment_success", response));
    }

    @PatchMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        commentService.updateComment(
                postId,
                commentId,
                request,
                userDetails.getUserId()
        );

        return ResponseEntity.ok(ApiResponse.success("update_comment_success"));
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        commentService.deleteComment(postId, commentId, userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.success("delete_comment_success"));
    }
}
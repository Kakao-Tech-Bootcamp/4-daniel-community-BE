package com.daniel.community.post.controller;

import com.daniel.community.global.response.ApiResponse;
import com.daniel.community.post.dto.PostImageUploadRequest;
import com.daniel.community.post.dto.PostImageUploadResponse;
import com.daniel.community.post.service.PostImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostImageController {

    private final PostImageService postImageService;

    public PostImageController(PostImageService postImageService) {
        this.postImageService = postImageService;
    }

    @PostMapping("/posts/images")
    public ResponseEntity<ApiResponse> uploadPostImage(
            @Valid @RequestBody PostImageUploadRequest request
    ) {
        PostImageUploadResponse response = postImageService.uploadPostImage(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("upload_success", response));
    }
}
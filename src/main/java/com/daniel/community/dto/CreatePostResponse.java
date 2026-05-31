package com.daniel.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatePostResponse {

    @JsonProperty("post_id")
    private Long postId;

    public CreatePostResponse(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }
}

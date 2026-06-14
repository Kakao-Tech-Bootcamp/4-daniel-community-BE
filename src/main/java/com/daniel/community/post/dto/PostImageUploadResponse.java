package com.daniel.community.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostImageUploadResponse {

    @JsonProperty("post_image")
    private String postImage;

    public PostImageUploadResponse(String postImage) {
        this.postImage = postImage;
    }

    public String getPostImage() {
        return postImage;
    }
}
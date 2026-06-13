package com.daniel.community.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class PostImageUploadRequest {

    @NotBlank
    @JsonProperty("post_image_name")
    private String postImageName;

    @NotBlank
    @JsonProperty("post_image_data")
    private String postImageData;

    public String getPostImageName() {
        return postImageName;
    }

    public String getPostImageData() {
        return postImageData;
    }
}
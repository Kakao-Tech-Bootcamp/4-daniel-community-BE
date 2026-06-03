package com.daniel.community.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class CreatePostRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @JsonProperty("post_image")
    private String postImage;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPostImage() {
        return postImage;
    }
}

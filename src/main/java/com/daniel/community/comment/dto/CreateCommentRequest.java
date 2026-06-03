package com.daniel.community.comment.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateCommentRequest {

    @NotBlank
    private String content;

    public String getContent() {
        return content;
    }
}
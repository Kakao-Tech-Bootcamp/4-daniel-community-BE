package com.daniel.community.comment.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateCommentRequest {

    @NotBlank
    private String content;

    public String getContent() {
        return content;
    }
}
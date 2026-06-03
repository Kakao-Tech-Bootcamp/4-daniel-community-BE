package com.daniel.community.comment.dto;

import com.daniel.community.comment.entity.Comment;
import com.daniel.community.post.dto.AuthorResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.format.DateTimeFormatter;

public class CommentResponse {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @JsonProperty("comment_id")
    private Long commentId;

    private String content;

    @JsonProperty("created_at")
    private String createdAt;

    private AuthorResponse author;

    public CommentResponse(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt().format(DATE_TIME_FORMATTER);
        this.author = new AuthorResponse(comment.getUser());
    }

    public Long getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public AuthorResponse getAuthor() {
        return author;
    }
}
package com.daniel.community.dto;

import com.daniel.community.entity.Post;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.format.DateTimeFormatter;

public class PostSummaryResponse {

    // 날짜를 문자열로 출력하기 위한 포맷지정
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @JsonProperty("post_id")
    private Long postId;

    private String title;

    private int likes;

    @JsonProperty("comments_count")
    private int commentsCount;

    private int views;

    @JsonProperty("created_at")
    private String createdAt;

    private AuthorResponse author;

    public PostSummaryResponse(Post post, int commentsCount) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.likes = post.getLikes();
        this.commentsCount = commentsCount;
        this.views = post.getViews();
        this.createdAt = post.getCreatedAt().format(DATE_TIME_FORMATTER);
        this.author = new AuthorResponse(post.getNickname(), post.getProfileImage());
    }

    public Long getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public int getLikes() {
        return likes;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public int getViews() {
        return views;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public AuthorResponse getAuthor() {
        return author;
    }
}

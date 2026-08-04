package com.daniel.community.post.dto;

import com.daniel.community.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostSummaryResponse {

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

    /*
     * 기존 PostService에서 사용하는 생성자입니다.
     * 서비스 수정이 완료될 때까지 유지합니다.
     */
    public PostSummaryResponse(
            Post post,
            int likes,
            int commentsCount
    ) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.likes = likes;
        this.commentsCount = commentsCount;
        this.views = post.getViews();
        this.createdAt =
                post.getCreatedAt().format(DATE_TIME_FORMATTER);
        this.author = new AuthorResponse(post.getUser());
    }

    /*
     * PostQueryRepository의 QueryDSL DTO Projection에서
     * 사용하는 생성자입니다.
     */
    public PostSummaryResponse(
            Long postId,
            String title,
            Long likes,
            Long commentsCount,
            Integer views,
            LocalDateTime createdAt,
            Long authorId,
            String nickname,
            String profileImage
    ) {
        this.postId = postId;
        this.title = title;
        this.likes = Math.toIntExact(likes);
        this.commentsCount = Math.toIntExact(commentsCount);
        this.views = views;
        this.createdAt =
                createdAt.format(DATE_TIME_FORMATTER);
        this.author =
                new AuthorResponse(
                        authorId,
                        nickname,
                        profileImage
                );
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
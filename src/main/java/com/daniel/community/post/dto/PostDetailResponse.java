package com.daniel.community.post.dto;

import com.daniel.community.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.format.DateTimeFormatter;

public class PostDetailResponse {

    // 날짜를 문자열로 출력하기 위한 포맷지정
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @JsonProperty("post_id")
    private Long postId;

    private String title;

    private String content;

    @JsonProperty("post_image")
    private String postImage;

    private int likes;

    private int views;

    @JsonProperty("comments_count")
    private int commentsCount;

    @JsonProperty("is_liked")
    private boolean isLiked;

    @JsonProperty("created_at")
    private String createdAt;

    private AuthorResponse author;

    // 게시글 자체의 정보는 Post에서 가져옴
    public PostDetailResponse(Post post, int likes, int commentsCount, boolean isLiked) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.postImage = post.getPostImage();
        this.likes = likes;
        this.views = post.getViews();
        this.commentsCount = commentsCount;
        this.isLiked = isLiked;
        this.createdAt = post.getCreatedAt().format(DATE_TIME_FORMATTER);
        this.author = new AuthorResponse(post.getUser());
    }

    public Long getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPostImage() {
        return postImage;
    }

    public int getLikes() {
        return likes;
    }

    public int getViews() {
        return views;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public AuthorResponse getAuthor() {
        return author;
    }
}
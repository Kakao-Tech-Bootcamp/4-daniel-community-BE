package com.daniel.community.post.entity;

import com.daniel.community.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "post_image")
    private String postImage;

    @Column(nullable = false)
    private int likes;

    @Column(nullable = false)
    private int views;

    // 게시글 하나가 사용자 한명에 속하는 관계
    @ManyToOne(fetch = FetchType.LAZY) // 작성자 정보를 실제로 필요할 때만 조회
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Post() {
    }

    public Post(String title, String content, String postImage, User user) {
        this.title = title;
        this.content = content;
        this.postImage = postImage;
        this.user = user;
        this.likes = 0;
        this.views = 0;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String title, String content, String postImage) {
        this.title = title;
        this.content = content;
        this.postImage = postImage;
    }

    public void increaseViews() {
        this.views++;
    }

    public boolean isWrittenBy(Long userId) {
        return this.user.getUserId().equals(userId);
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

    public User getUser() {
        return user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
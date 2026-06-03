package com.daniel.community.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PostListResponse {

    private List<PostSummaryResponse> posts;

    // 다음 게시글 목록을 가져올 때 사용할 기준 값
    @JsonProperty("next_cursor")
    private Long nextCursor;

    // 더 불러올 게시글이 있는지 알려주는 값
    @JsonProperty("has_more")
    private boolean hasMore;

    public PostListResponse(List<PostSummaryResponse> posts, Long nextCursor, boolean hasMore) {
        this.posts = posts;
        this.nextCursor = nextCursor;
        this.hasMore = hasMore;
    }

    public List<PostSummaryResponse> getPosts() {
        return posts;
    }

    public Long getNextCursor() {
        return nextCursor;
    }

    public boolean isHasMore() {
        return hasMore;
    }
}

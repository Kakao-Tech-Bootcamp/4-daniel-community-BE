package com.daniel.community.service;

import com.daniel.community.dto.CreatePostRequest;
import com.daniel.community.dto.CreatePostResponse;
import com.daniel.community.dto.PostDetailResponse;
import com.daniel.community.dto.PostListResponse;
import com.daniel.community.dto.PostSummaryResponse;
import com.daniel.community.dto.UpdatePostRequest;
import com.daniel.community.entity.Post;
import com.daniel.community.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// 서비스 계층
@Service
public class PostService {
    // 인증 인가 구현 이전이므로 임의의 사용자로 대체
    private static final String DEFAULT_NICKNAME = "startup";
    private static final String DEFAULT_PROFILE_IMAGE = "https://image.kr/img.jpg";

    private final PostRepository postRepository;

    // Service는 DB에 직접 접근하지 않고 Repository를 통해 접근
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    // createPostRequest 에서 값을 꺼내 Post 엔티티를 생성
    public CreatePostResponse createPost(CreatePostRequest request) {
        Post post = new Post(
                request.getTitle(),
                request.getContent(),
                request.getPostImage(),
                DEFAULT_NICKNAME,
                DEFAULT_PROFILE_IMAGE
        );

        Post savedPost = postRepository.save(post);
        // 저장 이후에 생성된 게시물의 ID를 응답으로 반환
        return new CreatePostResponse(savedPost.getPostId());
    }

    // 게시글 목록 조회
    @Transactional(readOnly = true)
    public PostListResponse getPosts(Long cursor) {
        List<Post> posts;

        // cursor가 없으면 최신 게시글 10개를 가져옴
        if (cursor == null) {
            posts = postRepository.findTop10ByOrderByPostIdDesc();
        // cursor가 있으면 해당 cursor 보다 이전 게시글 10개 가져옴
        } else {
            posts = postRepository.findTop10ByPostIdLessThanOrderByPostIdDesc(cursor);
        }

        List<PostSummaryResponse> postResponses = new ArrayList<>();

        for (Post post : posts) {
            postResponses.add(new PostSummaryResponse(post, 0));
        }

        Long nextCursor = null;

        if (!posts.isEmpty()) {
            Post lastPost = posts.get(posts.size() - 1);
            nextCursor = lastPost.getPostId();
        }

        // 10개가 왔다면 뒤에 데이터가 더 있을 거라고 생각하고 true로 설정
        boolean hasMore = posts.size() == 10;

        return new PostListResponse(postResponses, nextCursor, hasMore);
    }

    // 게시글 상세 조회
    @Transactional
    public PostDetailResponse getPost(Long postId) {
        Post post = findPost(postId);
        // 상세 조회에서 게시글을 조회하면 조회수를 올림
        post.increaseViews();

        return new PostDetailResponse(post, 0);
    }

    // 게시글 수정
    @Transactional
    public void updatePost(Long postId, UpdatePostRequest request) {
        Post post = findPost(postId);
        // Entity의 update 메서드 호출
        post.update(request.getTitle(), request.getContent(), request.getPostImage());
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = findPost(postId);
        // Entity의 delete 메서드 호출
        postRepository.delete(post);
    }

    // 게시글을 찾는 메서드
    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                // 게시글이 없을 경우 예외 처리
                .orElseThrow(() -> new IllegalArgumentException("post_not_found"));
    }
}

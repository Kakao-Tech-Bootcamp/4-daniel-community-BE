package com.daniel.community.post.service;

import com.daniel.community.post.dto.CreatePostRequest;
import com.daniel.community.post.dto.CreatePostResponse;
import com.daniel.community.post.dto.PostDetailResponse;
import com.daniel.community.post.dto.PostListResponse;
import com.daniel.community.post.dto.PostSummaryResponse;
import com.daniel.community.post.dto.UpdatePostRequest;
import com.daniel.community.post.entity.Post;
import com.daniel.community.user.entity.User;
import com.daniel.community.post.repository.PostRepository;
import com.daniel.community.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // Repository를 통해 DB 접근
    public PostService(
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    // createPostRequest에서 값을 꺼내 Post 엔티티를 생성
    public CreatePostResponse createPost(CreatePostRequest request, Long userId) {
        User user = findUser(userId);

        Post post = new Post(
                request.getTitle(),
                request.getContent(),
                request.getPostImage(),
                user
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
    public void updatePost(Long postId, UpdatePostRequest request, Long userId) {
        Post post = findPost(postId);

        // 게시글의 작성자인지 확인
        if (!post.isWrittenBy(userId)) {
            throw new IllegalArgumentException("forbidden");
        }
        // Entity의 update 메서드 호출
        post.update(request.getTitle(), request.getContent(), request.getPostImage());
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = findPost(postId);

        // 게시글의 작성자인지 확인
        if (!post.isWrittenBy(userId)) {
            throw new IllegalArgumentException("forbidden");
        }
        // Entity의 delete 메서드 호출
        postRepository.delete(post);
    }

    // 게시글 탐색
    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                // 게시글이 없을 경우 예외 처리
                .orElseThrow(() -> new IllegalArgumentException("post_not_found"));
    }

    // 사용자 탐색
    private User findUser(Long userId) {
        return userRepository.findById(userId)
                // 사용자가 없을 경우 예외 처리
                .orElseThrow(() -> new IllegalArgumentException("unauthorized"));
    }
}
package com.daniel.community.post.service;

import com.daniel.community.comment.repository.CommentRepository;
import com.daniel.community.post.dto.CreatePostRequest;
import com.daniel.community.post.dto.CreatePostResponse;
import com.daniel.community.post.dto.PostDetailResponse;
import com.daniel.community.post.dto.PostListResponse;
import com.daniel.community.post.dto.PostSummaryResponse;
import com.daniel.community.post.dto.UpdatePostRequest;
import com.daniel.community.post.entity.Post;
import com.daniel.community.post.repository.PostLikeRepository;
import com.daniel.community.post.repository.PostQueryRepository;
import com.daniel.community.post.repository.PostRepository;
import com.daniel.community.user.entity.User;
import com.daniel.community.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private static final int POST_PAGE_SIZE = 10;

    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    public PostService(
            PostRepository postRepository,
            PostQueryRepository postQueryRepository,
            UserRepository userRepository,
            CommentRepository commentRepository,
            PostLikeRepository postLikeRepository
    ) {
        this.postRepository = postRepository;
        this.postQueryRepository = postQueryRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.postLikeRepository = postLikeRepository;
    }

    @Transactional
    public CreatePostResponse createPost(
            CreatePostRequest request,
            Long userId
    ) {
        User user = findUser(userId);

        Post post = new Post(
                request.getTitle(),
                request.getContent(),
                request.getPostImage(),
                user
        );

        Post savedPost = postRepository.save(post);

        return new CreatePostResponse(savedPost.getPostId());
    }


    @Transactional(readOnly = true)
    public PostListResponse getPosts(Long cursor) {
        List<PostSummaryResponse> posts =
                postQueryRepository.findPostSummaries(
                        cursor,
                        null,
                        POST_PAGE_SIZE
                );

        return createPostListResponse(posts);
    }


    @Transactional(readOnly = true)
    public PostListResponse searchPosts(
            String keyword,
            Long cursor
    ) {
        if (keyword == null || keyword.isBlank()) {
            return getPosts(cursor);
        }

        List<PostSummaryResponse> posts =
                postQueryRepository.findPostSummaries(
                        cursor,
                        keyword,
                        POST_PAGE_SIZE
                );

        return createPostListResponse(posts);
    }


    @Transactional
    public PostDetailResponse getPost(
            Long postId,
            Long userId
    ) {
        Post post = findPost(postId);

        post.increaseViews();

        int likes = postLikeRepository.countByPost(post);
        int commentsCount =
                commentRepository.countByPost(post);

        boolean isLiked = false;

        if (userId != null) {
            User user = findUser(userId);

            isLiked =
                    postLikeRepository.existsByPostAndUser(
                            post,
                            user
                    );
        }

        return new PostDetailResponse(
                post,
                likes,
                commentsCount,
                isLiked
        );
    }


    @Transactional
    public void updatePost(
            Long postId,
            UpdatePostRequest request,
            Long userId
    ) {
        Post post = findPost(postId);

        if (!post.isWrittenBy(userId)) {
            throw new IllegalArgumentException("forbidden");
        }

        post.update(
                request.getTitle(),
                request.getContent(),
                request.getPostImage()
        );
    }


    @Transactional
    public void deletePost(
            Long postId,
            Long userId
    ) {
        Post post = findPost(postId);

        if (!post.isWrittenBy(userId)) {
            throw new IllegalArgumentException("forbidden");
        }

        postRepository.delete(post);
    }


    private PostListResponse createPostListResponse(
            List<PostSummaryResponse> queriedPosts
    ) {
        boolean hasMore =
                queriedPosts.size() > POST_PAGE_SIZE;

        int responseSize =
                Math.min(
                        queriedPosts.size(),
                        POST_PAGE_SIZE
                );

        List<PostSummaryResponse> responsePosts =
                new ArrayList<>(
                        queriedPosts.subList(
                                0,
                                responseSize
                        )
                );

        Long nextCursor = null;

        if (!responsePosts.isEmpty()) {
            PostSummaryResponse lastPost =
                    responsePosts.get(
                            responsePosts.size() - 1
                    );

            nextCursor = lastPost.getPostId();
        }

        return new PostListResponse(
                responsePosts,
                nextCursor,
                hasMore
        );
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "post_not_found"
                        )
                );
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "unauthorized"
                        )
                );
    }
}
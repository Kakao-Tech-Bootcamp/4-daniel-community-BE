package com.daniel.community.comment.service;

import com.daniel.community.comment.dto.CommentResponse;
import com.daniel.community.comment.dto.CreateCommentRequest;
import com.daniel.community.comment.dto.CreateCommentResponse;
import com.daniel.community.comment.dto.UpdateCommentRequest;
import com.daniel.community.comment.entity.Comment;
import com.daniel.community.comment.repository.CommentRepository;
import com.daniel.community.post.entity.Post;
import com.daniel.community.post.repository.PostRepository;
import com.daniel.community.user.entity.User;
import com.daniel.community.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 게시글 탐색 이후 해당 게시글의 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {
        Post post = findPost(postId);

        List<Comment> comments = commentRepository.findByPostOrderByCommentIdAsc(post);
        List<CommentResponse> responses = new ArrayList<>();

        for (Comment comment : comments) {
            responses.add(new CommentResponse(comment));
        }

        return responses;
    }

    @Transactional
    public CreateCommentResponse createComment(
            Long postId,
            CreateCommentRequest request,
            Long userId
    ) {
        // 게시글과 사용자 검증 이후 댓글을 생성
        Post post = findPost(postId);
        User user = findUser(userId);

        Comment comment = new Comment(request.getContent(), post, user);
        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponse(savedComment.getCommentId());
    }

    @Transactional
    public void updateComment(
            Long postId,
            Long commentId,
            UpdateCommentRequest request,
            Long userId
    ) {
        Post post = findPost(postId);
        Comment comment = findComment(commentId);

        checkCommentInPost(comment, post);

        if (!comment.isWrittenBy(userId)) {
            throw new IllegalArgumentException("forbidden");
        }

        comment.update(request.getContent());
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, Long userId) {
        Post post = findPost(postId);
        Comment comment = findComment(commentId);

        checkCommentInPost(comment, post);

        if (!comment.isWrittenBy(userId)) {
            throw new IllegalArgumentException("forbidden");
        }

        commentRepository.delete(comment);
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post_not_found"));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("unauthorized"));
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("comment_not_found"));
    }

    // URL의 PostId와 댓글이 존재하는 게시글이 일치하는지 확인
    private void checkCommentInPost(Comment comment, Post post) {
        if (!comment.getPost().getPostId().equals(post.getPostId())) {
            throw new IllegalArgumentException("comment_not_found");
        }
    }
}
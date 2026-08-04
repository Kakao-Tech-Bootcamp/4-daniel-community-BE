package com.daniel.community.post.repository;

import com.daniel.community.comment.entity.QComment;
import com.daniel.community.post.dto.PostSummaryResponse;
import com.daniel.community.post.entity.QPost;
import com.daniel.community.post.entity.QPostLike;
import com.daniel.community.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    public PostQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<PostSummaryResponse> findPostSummaries(
            Long cursor,
            String keyword,
            int limit
    ) {
        QPost post = QPost.post;
        QUser user = QUser.user;
        QPostLike postLike = QPostLike.postLike;
        QComment comment = QComment.comment;

        BooleanBuilder condition = new BooleanBuilder();

        if (cursor != null) {
            condition.and(post.postId.lt(cursor));
        }

        if (keyword != null && !keyword.isBlank()) {
            condition.and(
                    post.title.contains(keyword)
                            .or(post.content.contains(keyword))
            );
        }

        return queryFactory
                .select(
                        Projections.constructor(
                                PostSummaryResponse.class,
                                post.postId,
                                post.title,
                                JPAExpressions
                                        .select(postLike.count())
                                        .from(postLike)
                                        .where(postLike.post.eq(post)),
                                JPAExpressions
                                        .select(comment.count())
                                        .from(comment)
                                        .where(comment.post.eq(post)),
                                post.views,
                                post.createdAt,
                                user.userId,
                                user.nickname,
                                user.profileImage
                        )
                )
                .from(post)
                .join(post.user, user)
                .where(condition)
                .orderBy(post.postId.desc())
                .limit(limit + 1L)
                .fetch();
    }
}
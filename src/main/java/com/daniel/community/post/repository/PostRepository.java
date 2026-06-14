package com.daniel.community.post.repository;

import com.daniel.community.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 처음 게시판에 들어왔을 때 최신 게시글 10개를 가져오는 메서드
    List<Post> findTop10ByOrderByPostIdDesc();

    // 무한 스크롤에서 다음 게시글 10개를 가져오기 위한 메서드
    List<Post> findTop10ByPostIdLessThanOrderByPostIdDesc(Long cursor);

    // 검색어가 제목 또는 내용에 포함된 최신 게시글 10개 조회
    @Query("""
            select post
            from Post post
            where post.title like concat('%', :keyword, '%')
               or post.content like concat('%', :keyword, '%')
            order by post.postId desc
            """)
    List<Post> searchTop10ByKeyword(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // 검색 결과에서 cursor보다 오래된 게시글 10개 조회
    @Query("""
            select post
            from Post post
            where post.postId < :cursor
              and (
                   post.title like concat('%', :keyword, '%')
                or post.content like concat('%', :keyword, '%')
              )
            order by post.postId desc
            """)
    List<Post> searchTop10ByKeywordAndCursor(
            @Param("keyword") String keyword,
            @Param("cursor") Long cursor,
            Pageable pageable
    );
}
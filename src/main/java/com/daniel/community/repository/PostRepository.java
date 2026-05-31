package com.daniel.community.repository;

import com.daniel.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {

    // 처음 게시판에 들어왔을 때 최신 게시글 10개를 가져오는 메서드
    List<Post> findTop10ByOrderByPostIdDesc();

    // 무한 스크롤에서 다음 게시글 10개를 가져오기 위한 메서드
    List<Post> findTop10ByPostIdLessThanOrderByPostIdDesc(Long cursor);
}

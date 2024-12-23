package com.example.repository;

import com.example.entity.Like;
import com.example.entity.Post;
import com.example.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    long countByPost_Id(Long postId);
    long countByComment_Id(Long commentId);
    boolean existsByPostAndUser(Post post, User user);
}

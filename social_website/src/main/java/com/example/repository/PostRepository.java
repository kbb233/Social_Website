package com.example.repository;

import com.example.entity.Post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser_Username(String username);
    List<Post> findByContentContaining(String keyword);
    List<Post> findByContentContaining(String keyword, Pageable pageable);
}

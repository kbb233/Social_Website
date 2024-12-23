package com.example.repository;

import com.example.entity.Follower;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    long countByFollowed_Id(Long id);
    long countByFollower_Id(Long id);
    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);
    Optional<Follower> findByFollowerIdAndFollowedId(Long followerId, Long followedId);
    List<Follower> findAllByFollowerId(Long followerId);
    List<Follower> findAllByFollowedId(Long followedId);

}

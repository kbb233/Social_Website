package com.example.service;

import com.example.entity.Follower;
import com.example.entity.User;
import com.example.repository.FollowerRepository;
import com.example.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FollowerService {

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private UserRepository userRepository;

    public Follower followUser(Long followerId, Long followedId) {
        Optional<User> follower = userRepository.findById(followerId);
        Optional<User> followed = userRepository.findById(followedId);
        if (follower.isPresent() && followed.isPresent()) {
            Follower newFollower = new Follower();
            newFollower.setFollower(follower.get());
            newFollower.setFollowed(followed.get());
            return followerRepository.save(newFollower);
        }
        throw new IllegalArgumentException("Invalid follower or followed user");
    }
}

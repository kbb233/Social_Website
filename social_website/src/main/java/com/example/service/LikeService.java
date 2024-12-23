package com.example.service;

import com.example.entity.Like;
import com.example.entity.Post;
import com.example.entity.User;
import com.example.repository.LikeRepository;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public void likePost(Long postId, Long userId) {
        Optional<Post> post = postRepository.findById(postId);
        Optional<User> user = userRepository.findById(userId);
        if (post.isPresent() && user.isPresent()) {
            Like like = new Like();
            like.setPost(post.get());
            like.setUser(user.get());
            likeRepository.save(like);
        } else {
            throw new IllegalArgumentException("Post or User not found");
        }
    }
}

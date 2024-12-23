package com.example.service;

import com.example.entity.Comment;
import com.example.entity.Post;
import com.example.repository.CommentRepository;
import com.example.repository.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    public Comment addComment(Long postId, Comment comment) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            comment.setPost(post.get());
            return commentRepository.save(comment);
        }
        throw new IllegalArgumentException("Post not found");
    }
}

package com.example.controller;

import java.sql.Timestamp;
import java.util.List;

import com.example.entity.Comment;
import com.example.entity.Post;

public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private String username; 
    private Timestamp createdAt;
    private int likedNumber;
    private List<Comment> comments;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getUsername();
        this.createdAt = post.getCreatedAt();
        this.comments = post.getComments();
        this.likedNumber = post.getLikedNumber();
    }


    public int getLikedNumber() {
        return this.likedNumber;
    }

    public void setLikedNumber(int likedNumber) {
        this.likedNumber = likedNumber;
    }

    public List<Comment> getComments() {
        return this.comments;
    }


    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
}

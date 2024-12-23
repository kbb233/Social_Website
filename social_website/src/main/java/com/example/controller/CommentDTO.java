package com.example.controller;

import java.sql.Timestamp;
import com.example.entity.Comment;

public class CommentDTO {
    private Long userId;
    private String content;
    private int likedNumber;
    private Timestamp createdAt;
    
    public CommentDTO(){};

    public CommentDTO(Comment comment) {
        this.userId = comment.getUser().getId();
        this.content = comment.getContent();
        this.likedNumber = comment.getLikedNumber();
        this.createdAt = comment.getCreatedAt();
    }



    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikedNumber() {
        return this.likedNumber;
    }

    public void setLikedNumber(int likedNumber) {
        this.likedNumber = likedNumber;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
}

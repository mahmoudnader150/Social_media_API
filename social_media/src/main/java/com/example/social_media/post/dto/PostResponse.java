package com.example.social_media.post.dto;

import com.example.social_media.post.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.example.social_media.comment.dto.CommentResponse;

public class PostResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String userName;
    private Long userId;
    private int commentCount;
    private int likesCount;
    private List<CommentResponse> comments;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.userName = post.getUser().getName();
        this.userId = post.getUser().getId();
        this.commentCount = post.getComments().size();
        this.likesCount = post.getLikes() != null ? post.getLikes().size() : 0;
        this.comments = post.getComments() != null ? post.getComments().stream().map(CommentResponse::new).collect(Collectors.toList()) : null;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public List<CommentResponse> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponse> comments) {
        this.comments = comments;
    }
}
package com.example.social_media.user;

import java.time.LocalDateTime;

public class UserStatusUpdate {
    private Long userId;
    private String userName;
    private boolean isOnline;
    private LocalDateTime lastSeen;

    public UserStatusUpdate(Long userId, String userName, boolean isOnline, LocalDateTime lastSeen) {
        this.userId = userId;
        this.userName = userName;
        this.isOnline = isOnline;
        this.lastSeen = lastSeen;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }
} 
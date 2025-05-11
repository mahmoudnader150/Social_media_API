package com.example.social_media.notification;

import java.time.LocalDateTime;

public class NotificationDTO {
    private Long id;
    private String message;
    private NotificationType type;
    private Long referenceId;
    private String referenceUrl;
    private boolean read;
    private LocalDateTime createdAt;

    // Constructors
    public NotificationDTO() {
    }

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.type = notification.getType();
        this.referenceId = notification.getReferenceId();
        this.referenceUrl = notification.getReferenceUrl();
        this.read = notification.isRead();
        this.createdAt = notification.getCreatedAt();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceUrl() {
        return referenceUrl;
    }

    public void setReferenceUrl(String referenceUrl) {
        this.referenceUrl = referenceUrl;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 
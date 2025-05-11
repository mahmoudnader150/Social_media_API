package com.example.social_media.user.dto;

import com.example.social_media.media.Media;
import com.example.social_media.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSummaryDTO {
    private Long id;
    private String name;
    private String email;
    private UserProfileDTO.MediaDTO profilePicture;
    private boolean isOnline;

    public UserSummaryDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.isOnline = user.isOnline();
        
        if (user.getProfilePicture() != null) {
            this.profilePicture = new UserProfileDTO.MediaDTO(user.getProfilePicture());
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserProfileDTO.MediaDTO getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(UserProfileDTO.MediaDTO profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
} 
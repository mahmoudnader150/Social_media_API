package com.example.social_media.user.dto;

import com.example.social_media.media.Media;
import com.example.social_media.user.User;
import com.example.social_media.user.UserFollowing;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime birthDate;
    private MediaDTO profilePicture;
    private boolean isOnline;
    private LocalDateTime lastSeen;
    private int followersCount;
    private int followingCount;
    private List<UserSummaryDTO> followers;
    private List<UserSummaryDTO> following;
    private int postsCount;
    private int likesCount;
    private int commentsCount;

    public UserProfileDTO(User user, boolean includeFollowers, boolean includeFollowing) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.birthDate = user.getBirthDate();
        this.isOnline = user.isOnline();
        this.lastSeen = user.getLastSeen();
        
        if (user.getProfilePicture() != null) {
            this.profilePicture = new MediaDTO(user.getProfilePicture());
        }
        
        this.followersCount = user.getFollowers().size();
        this.followingCount = user.getFollowing().size();
        
        if (includeFollowers) {
            this.followers = user.getFollowers().stream()
                    .map(following -> new UserSummaryDTO(following.getFollower()))
                    .collect(Collectors.toList());
        }
        
        if (includeFollowing) {
            this.following = user.getFollowing().stream()
                    .map(following -> new UserSummaryDTO(following.getFollowing()))
                    .collect(Collectors.toList());
        }
        
        this.postsCount = user.getPosts().size();
        this.commentsCount = user.getComments().size();
        
        // Calculate total likes received across all posts
        this.likesCount = user.getPosts().stream()
                .mapToInt(post -> post.getLikes().size())
                .sum();
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

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public MediaDTO getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(MediaDTO profilePicture) {
        this.profilePicture = profilePicture;
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

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public List<UserSummaryDTO> getFollowers() {
        return followers;
    }

    public void setFollowers(List<UserSummaryDTO> followers) {
        this.followers = followers;
    }

    public List<UserSummaryDTO> getFollowing() {
        return following;
    }

    public void setFollowing(List<UserSummaryDTO> following) {
        this.following = following;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(int postsCount) {
        this.postsCount = postsCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    // Inner class for Media data
    public static class MediaDTO {
        private Long id;
        private String filePath;
        private String fileType;

        public MediaDTO(Media media) {
            this.id = media.getId();
            this.filePath = media.getFilePath();
            this.fileType = media.getFileType();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }
    }
} 
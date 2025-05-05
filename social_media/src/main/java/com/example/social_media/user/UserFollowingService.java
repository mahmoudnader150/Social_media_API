package com.example.social_media.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserFollowingService {
    
    private final UserFollowingRepository userFollowingRepository;
    private final UserService userService;

    @Autowired
    public UserFollowingService(UserFollowingRepository userFollowingRepository, UserService userService) {
        this.userFollowingRepository = userFollowingRepository;
        this.userService = userService;
    }

    /**
     * Follow a user
     * @param followerId ID of the user who is following
     * @param followingId ID of the user to follow
     * @return the created UserFollowing entity
     */
    public UserFollowing followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("Users cannot follow themselves");
        }

        // Check if already following
        if (userFollowingRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new IllegalStateException("User is already following this user");
        }

        User follower = userService.findById(followerId);
        User following = userService.findById(followingId);

        UserFollowing userFollowing = new UserFollowing(follower, following);
        return userFollowingRepository.save(userFollowing);
    }

    /**
     * Unfollow a user
     * @param followerId ID of the user who is unfollowing
     * @param followingId ID of the user to unfollow
     */
    public void unfollowUser(Long followerId, Long followingId) {
        if (!userFollowingRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new IllegalStateException("User is not following this user");
        }
        
        userFollowingRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }

    /**
     * Check if a user follows another user
     * @param followerId ID of the potential follower
     * @param followingId ID of the potential followee
     * @return true if following, false otherwise
     */
    public boolean isFollowing(Long followerId, Long followingId) {
        return userFollowingRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    /**
     * Get all users that a user is following
     * @param userId The user ID
     * @return List of users being followed
     */
    public List<User> getFollowing(Long userId) {
        List<UserFollowing> followingRelations = userFollowingRepository.findByFollowerId(userId);
        return followingRelations.stream()
                .map(UserFollowing::getFollowing)
                .collect(Collectors.toList());
    }

    /**
     * Get all followers of a user
     * @param userId The user ID
     * @return List of followers
     */
    public List<User> getFollowers(Long userId) {
        List<UserFollowing> followerRelations = userFollowingRepository.findByFollowingId(userId);
        return followerRelations.stream()
                .map(UserFollowing::getFollower)
                .collect(Collectors.toList());
    }

    /**
     * Count how many users the given user is following
     * @param userId The user ID
     * @return Count of users being followed
     */
    public long countFollowing(Long userId) {
        return userFollowingRepository.countByFollowerId(userId);
    }

    /**
     * Count how many followers a user has
     * @param userId The user ID
     * @return Count of followers
     */
    public long countFollowers(Long userId) {
        return userFollowingRepository.countByFollowingId(userId);
    }
}
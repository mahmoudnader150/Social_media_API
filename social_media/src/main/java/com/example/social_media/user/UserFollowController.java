package com.example.social_media.user;

import com.example.social_media.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserFollowController {

    private final UserFollowingService userFollowingService;
    private final UserService userService;

    @Autowired
    public UserFollowController(UserFollowingService userFollowingService, UserService userService) {
        this.userFollowingService = userFollowingService;
        this.userService = userService;
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<?> followUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        
        try {
            Long currentUserId = currentUser.getUser().getId();
            userFollowingService.followUser(currentUserId, userId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "You are now following " + userService.findById(userId).getName());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/unfollow")
    public ResponseEntity<?> unfollowUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        
        try {
            Long currentUserId = currentUser.getUser().getId();
            userFollowingService.unfollowUser(currentUserId, userId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "You have unfollowed " + userService.findById(userId).getName());
            
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<User>> getUserFollowers(@PathVariable Long userId) {
        List<User> followers = userFollowingService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<User>> getUserFollowing(@PathVariable Long userId) {
        List<User> following = userFollowingService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Map<String, Long>> countUserFollowers(@PathVariable Long userId) {
        long count = userFollowingService.countFollowers(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Map<String, Long>> countUserFollowing(@PathVariable Long userId) {
        long count = userFollowingService.countFollowing(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/{userId}/is-following")
    public ResponseEntity<Map<String, Boolean>> checkIfFollowing(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        
        Long currentUserId = currentUser.getUser().getId();
        boolean isFollowing = userFollowingService.isFollowing(currentUserId, userId);
        
        return ResponseEntity.ok(Map.of("following", isFollowing));
    }
}
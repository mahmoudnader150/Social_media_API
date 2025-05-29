package com.example.social_media.user;

import com.example.social_media.security.CustomUserDetails;
import com.example.social_media.user.dto.UpdateProfileRequest;
import com.example.social_media.user.dto.UserProfileDTO;
import com.example.social_media.user.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        return ResponseEntity.ok(userService.searchByName(query));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<UserStatusUpdate> getUserStatus(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(new UserStatusUpdate(
            user.getId(),
            user.getName(),
            user.isOnline(),
            user.getLastSeen()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (!currentUser.getUser().getId().equals(id)) {
            return ResponseEntity.badRequest().body("You can only delete your own account");
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get comprehensive user profile data including followers, following, posts counts, etc.
     *
     * @param id ID of the user to get profile for
     * @param includeFollowers Whether to include full follower details (optional, default=false)
     * @param includeFollowing Whether to include full following details (optional, default=false)
     * @return UserProfileDTO with comprehensive user data
     */
    @GetMapping("/{id}/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean includeFollowers,
            @RequestParam(defaultValue = "false") boolean includeFollowing) {
        
        UserProfileDTO profile = userService.getUserProfile(id, includeFollowers, includeFollowing);
        return ResponseEntity.ok(profile);
    }

    /**
     * Get current user's profile data
     * 
     * @param currentUser The authenticated user
     * @param includeFollowers Whether to include full follower details (optional, default=false)
     * @param includeFollowing Whether to include full following details (optional, default=false)
     * @return UserProfileDTO with comprehensive user data
     */
    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileDTO> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(defaultValue = "false") boolean includeFollowers,
            @RequestParam(defaultValue = "false") boolean includeFollowing) {
        
        UserProfileDTO profile = userService.getUserProfile(
                currentUser.getUser().getId(), 
                includeFollowers, 
                includeFollowing);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me/profile")
    public ResponseEntity<UserResponse> updateMyProfile(
            @Validated @RequestBody UpdateProfileRequest request,
            @AuthenticationPrincipal com.example.social_media.security.CustomUserDetails currentUser) {
        UserResponse updated = userService.updateProfile(currentUser.getUser(), request);
        return ResponseEntity.ok(updated);
    }
}
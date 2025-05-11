package com.example.social_media.saved;

import com.example.social_media.post.Post;
import com.example.social_media.post.dto.PostResponse;
import com.example.social_media.security.CustomUserDetails;
import com.example.social_media.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/saved-posts")
public class SavedPostController {
    private final SavedPostService savedPostService;

    public SavedPostController(SavedPostService savedPostService) {
        this.savedPostService = savedPostService;
    }

    /**
     * Save a post for the current user
     * 
     * @param postId ID of the post to save
     * @param currentUser Currently authenticated user
     * @return Success message
     */
    @PostMapping("/{postId}")
    public ResponseEntity<?> savePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        savedPostService.savePost(currentUser.getUser(), postId);
        return ResponseEntity.ok(Map.of("message", "Post saved successfully"));
    }

    /**
     * Unsave a post for the current user
     * 
     * @param postId ID of the post to unsave
     * @param currentUser Currently authenticated user
     * @return No content
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> unsavePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        savedPostService.unsavePost(currentUser.getUser(), postId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if a post is saved by the current user
     * 
     * @param postId ID of the post to check
     * @param currentUser Currently authenticated user
     * @return Boolean indicating if the post is saved
     */
    @GetMapping("/{postId}/status")
    public ResponseEntity<Map<String, Boolean>> checkSavedStatus(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        boolean isSaved = savedPostService.isPostSavedByUser(currentUser.getUser(), postId);
        return ResponseEntity.ok(Map.of("saved", isSaved));
    }

    /**
     * Get all posts saved by the current user
     * 
     * @param page Page number (0-based)
     * @param size Page size
     * @param currentUser Currently authenticated user
     * @return Page of PostResponse objects
     */
    @GetMapping
    public ResponseEntity<Page<PostResponse>> getSavedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        Page<SavedPost> savedPosts = savedPostService.getSavedPosts(
                currentUser.getUser(),
                PageRequest.of(page, size, Sort.by("savedAt").descending())
        );
        
        // Convert SavedPost to PostResponse
        Page<PostResponse> postResponses = savedPosts.map(savedPost -> new PostResponse(savedPost.getPost()));
        return ResponseEntity.ok(postResponses);
    }
} 
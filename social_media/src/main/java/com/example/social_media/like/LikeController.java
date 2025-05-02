package com.example.social_media.like;

import com.example.social_media.post.Post;
import com.example.social_media.post.PostService;
import com.example.social_media.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts/{postId}/likes")
public class LikeController {
    private final LikeService likeService;
    private final PostService postService;

    public LikeController(LikeService likeService, PostService postService) {
        this.likeService = likeService;
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<?> likePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        Post post = postService.findById(postId);
        
        if (likeService.hasUserLikedPost(post, currentUser.getUser())) {
            return ResponseEntity.badRequest().body("You have already liked this post");
        }
        
        likeService.likePost(post, currentUser.getUser());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> unlikePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        Post post = postService.findById(postId);
        
        if (!likeService.hasUserLikedPost(post, currentUser.getUser())) {
            return ResponseEntity.badRequest().body("You have not liked this post");
        }
        
        likeService.unlikePost(post, currentUser.getUser());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long postId) {
        Post post = postService.findById(postId);
        long count = likeService.getLikeCount(post);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> hasUserLikedPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        Post post = postService.findById(postId);
        boolean hasLiked = likeService.hasUserLikedPost(post, currentUser.getUser());
        return ResponseEntity.ok(hasLiked);
    }
} 
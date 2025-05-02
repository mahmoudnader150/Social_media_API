package com.example.social_media.comment;

import com.example.social_media.post.Post;
import com.example.social_media.post.PostService;
import com.example.social_media.security.CustomUserDetails;
import com.example.social_media.user.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getCommentsForPost(@PathVariable Long postId) {
        Post post = postService.findById(postId);
        List<Comment> comments = commentService.findByPost(post);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody Comment comment) {
        Post post = postService.findById(postId);
        comment.setPost(post);
        comment.setUser(currentUser.getUser());
        
        Comment savedComment = commentService.save(comment);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedComment.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedComment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody Comment commentRequest) {
        Post post = postService.findById(postId);
        Comment comment = commentService.findById(commentId);
        
        if (!comment.getUser().getId().equals(currentUser.getUser().getId())) {
            throw new CommentAccessDeniedException("You can only update your own comments");
        }
        
        comment.setContent(commentRequest.getContent());
        Comment updatedComment = commentService.save(comment);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        Comment comment = commentService.findById(commentId);
        
        if (!comment.getUser().getId().equals(currentUser.getUser().getId())) {
            throw new CommentAccessDeniedException("You can only delete your own comments");
        }
        
        commentService.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }
} 
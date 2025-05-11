package com.example.social_media.post;

import com.example.social_media.notification.NotificationService;
import com.example.social_media.post.dto.PostRequest;
import com.example.social_media.post.dto.PostResponse;
import com.example.social_media.security.CustomUserDetails;
import com.example.social_media.user.User;
import com.example.social_media.user.UserService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final NotificationService notificationService;

    public PostController(
            PostService postService, 
            @Lazy UserService userService,
            NotificationService notificationService) {
        this.postService = postService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postService.findAll(pageRequest);
        Page<PostResponse> responses = posts.map(PostResponse::new);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PostResponse>> searchPosts(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postService.searchByContent(query, pageRequest);
        Page<PostResponse> responses = posts.map(PostResponse::new);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        Post post = postService.findByIdWithComments(id);
        return ResponseEntity.ok(new PostResponse(post));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getUserPosts(@PathVariable Long userId) {
        User user = userService.findById(userId);
        List<Post> posts = postService.findByUser(user);
        List<PostResponse> responses = posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody PostRequest postRequest) {
        Post post = new Post();
        post.setContent(postRequest.getContent());
        post.setUser(currentUser.getUser());
        
        Post savedPost = postService.save(post);
        
        // Send notifications to followers about new post
        notificationService.notifyNewPost(currentUser.getUser(), savedPost.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(new PostResponse(savedPost));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id,
            @Valid @RequestBody PostRequest postRequest) {
        Post post = new Post();
        post.setContent(postRequest.getContent());
        
        Post updatedPost = postService.update(id, post, currentUser.getUser());
        return ResponseEntity.ok(new PostResponse(updatedPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        postService.deleteById(id, currentUser.getUser());
        return ResponseEntity.noContent().build();
    }
} 
package com.example.social_media.post;

import com.example.social_media.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody Post post) {
        post.setUser(currentUser.getUser());
        Post savedPost = postService.save(post);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id,
            @Valid @RequestBody Post post) {
        Post existingPost = postService.findById(id);
        
        if (!existingPost.getUser().getId().equals(currentUser.getUser().getId())) {
            return ResponseEntity.forbidden().build();
        }

        existingPost.setContent(post.getContent());
        return ResponseEntity.ok(postService.save(existingPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        Post post = postService.findById(id);
        
        if (!post.getUser().getId().equals(currentUser.getUser().getId())) {
            return ResponseEntity.forbidden().build();
        }

        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 
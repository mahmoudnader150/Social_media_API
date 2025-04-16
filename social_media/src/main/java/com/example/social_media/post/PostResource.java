package com.example.social_media.post;

import com.example.social_media.user.User;
import com.example.social_media.user.UserDaoService;
import com.example.social_media.user.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class PostResource {

    private final PostDaoService postDaoService;
    private final UserDaoService userDaoService;

    public PostResource(PostDaoService postDaoService, UserDaoService userDaoService) {
        this.postDaoService = postDaoService;
        this.userDaoService = userDaoService;
    }

    @GetMapping("/posts")
    public List<Post> retrieveAllPosts() {
        return postDaoService.findAll();
    }

    @GetMapping("/users/{userId}/posts")
    public List<Post> retrievePostsForUser(@PathVariable Integer userId) {
        User user = userDaoService.findOne(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        return postDaoService.findByUser(user);
    }

    @GetMapping("/posts/{postId}")
    public Post retrievePost(@PathVariable Long postId) {
        Post post = postDaoService.findOne(postId);
        if (post == null) {
            throw new PostNotFoundException("Post not found with id: " + postId);
        }
        return post;
    }

    @PostMapping("/users/{userId}/posts")
    public ResponseEntity<Post> createPost(@PathVariable Integer userId, @Valid @RequestBody Post post) {
        User user = userDaoService.findOne(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        
        post.setUser(user);
        Post savedPost = postDaoService.save(post);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedPost);
    }

    @DeleteMapping("/posts/{postId}")
    public void deletePost(@PathVariable Long postId) {
        Post post = postDaoService.findOne(postId);
        if (post == null) {
            throw new PostNotFoundException("Post not found with id: " + postId);
        }
        postDaoService.deleteById(postId);
    }
} 
package com.example.social_media.post;

import com.example.social_media.user.User;
import com.example.social_media.user.UserRepository;
import com.example.social_media.user.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class PostResource {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResource(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/users/{userId}/posts")
    public List<Post> retrievePostsForUser(@PathVariable Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("id:" + userId);
        }
        return postRepository.findByUserOrderByCreatedAtDesc(userOptional.get());
    }

    @GetMapping("/users/{userId}/posts/{id}")
    public EntityModel<Post> retrievePostForUser(@PathVariable Long userId, @PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("id:" + userId);
        }

        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new PostNotFoundException("id:" + id);
        }

        EntityModel<Post> entityModel = EntityModel.of(post.get());
        WebMvcLinkBuilder link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrievePostsForUser(userId));
        entityModel.add(link.withRel("all-posts"));

        return entityModel;
    }

    @PostMapping("/users/{userId}/posts")
    public ResponseEntity<Post> createPost(@PathVariable Long userId, @Valid @RequestBody Post post) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("id:" + userId);
        }

        post.setUser(userOptional.get());
        Post savedPost = postRepository.save(post);

        URI location = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrievePostForUser(userId, savedPost.getId())).toUri();
        return ResponseEntity.created(location).build();
    }
} 
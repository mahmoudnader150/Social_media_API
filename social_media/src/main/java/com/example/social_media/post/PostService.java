package com.example.social_media.post;

import com.example.social_media.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));
    }

    public Post findByIdWithComments(Long id) {
        Post post = postRepository.findByIdWithComments(id);
        if (post == null) {
            throw new PostNotFoundException("Post not found with id: " + id);
        }
        return post;
    }

    public List<Post> findByUser(User user) {
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Post update(Long id, Post postRequest, User currentUser) {
        Post post = findById(id);
        
        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new PostAccessDeniedException("You can only update your own posts");
        }
        
        post.setContent(postRequest.getContent());
        return postRepository.save(post);
    }

    public void deleteById(Long id, User currentUser) {
        Post post = findById(id);
        
        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new PostAccessDeniedException("You can only delete your own posts");
        }
        
        postRepository.deleteById(id);
    }

    public boolean existsByIdAndUser(Long postId, User user) {
        return postRepository.existsByIdAndUserId(postId, user.getId());
    }

    public Page<Post> searchByContent(String searchTerm, Pageable pageable) {
        return postRepository.searchByContent(searchTerm, pageable);
    }
} 
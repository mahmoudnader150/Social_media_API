package com.example.social_media.saved;

import com.example.social_media.post.Post;
import com.example.social_media.post.PostService;
import com.example.social_media.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SavedPostService {
    private final SavedPostRepository savedPostRepository;
    private final PostService postService;

    public SavedPostService(SavedPostRepository savedPostRepository, PostService postService) {
        this.savedPostRepository = savedPostRepository;
        this.postService = postService;
    }

    /**
     * Save a post for a user
     * 
     * @param user The user saving the post
     * @param postId The ID of the post to save
     * @return The newly created SavedPost entity
     */
    public SavedPost savePost(User user, Long postId) {
        Post post = postService.findById(postId);
        
        // Check if already saved
        if (savedPostRepository.existsByUserAndPost(user, post)) {
            throw new IllegalStateException("Post already saved by user");
        }
        
        SavedPost savedPost = new SavedPost(user, post);
        return savedPostRepository.save(savedPost);
    }

    /**
     * Unsave/remove a saved post
     * 
     * @param user The user unsaving the post
     * @param postId The ID of the post to unsave
     */
    public void unsavePost(User user, Long postId) {
        Post post = postService.findById(postId);
        savedPostRepository.deleteByUserAndPost(user, post);
    }

    /**
     * Check if a post is saved by a user
     * 
     * @param user The user to check
     * @param postId The post ID to check
     * @return true if saved, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean isPostSavedByUser(User user, Long postId) {
        Post post = postService.findById(postId);
        return savedPostRepository.existsByUserAndPost(user, post);
    }

    /**
     * Get all saved posts for a user
     * 
     * @param user The user whose saved posts to retrieve
     * @param pageable Pagination information
     * @return Page of saved posts
     */
    @Transactional(readOnly = true)
    public Page<SavedPost> getSavedPosts(User user, Pageable pageable) {
        return savedPostRepository.findByUserOrderBySavedAtDesc(user, pageable);
    }
} 
package com.example.social_media.saved;

import com.example.social_media.post.Post;
import com.example.social_media.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedPostRepository extends JpaRepository<SavedPost, Long> {
    Page<SavedPost> findByUserOrderBySavedAtDesc(User user, Pageable pageable);
    
    boolean existsByUserAndPost(User user, Post post);
    
    void deleteByUserAndPost(User user, Post post);
} 
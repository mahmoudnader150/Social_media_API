package com.example.social_media.post;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Integer userId);
    List<Post> findByUserOrderByCreatedAtDesc(User user);
} 
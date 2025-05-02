package com.example.social_media.like;

import com.example.social_media.post.Post;
import com.example.social_media.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, User user);
    long countByPost(Post post);
    void deleteByPostAndUser(Post post, User user);
} 
package com.example.social_media.comment;

import com.example.social_media.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);
    List<Comment> findByUserIdOrderByCreatedAtDesc(Integer userId);
} 
package com.example.social_media.comment;

import com.example.social_media.post.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> findByPost(Post post) {
        return commentRepository.findByPostOrderByCreatedAtDesc(post);
    }

    public List<Comment> findByUserId(Integer userId) {
        return commentRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
} 
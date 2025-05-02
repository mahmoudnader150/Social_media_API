package com.example.social_media.like;

import com.example.social_media.post.Post;
import com.example.social_media.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public Like likePost(Post post, User user) {
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        return likeRepository.save(like);
    }

    public void unlikePost(Post post, User user) {
        likeRepository.deleteByPostAndUser(post, user);
    }

    public long getLikeCount(Post post) {
        return likeRepository.countByPost(post);
    }

    public boolean hasUserLikedPost(Post post, User user) {
        return likeRepository.findByPostAndUser(post, user).isPresent();
    }
} 
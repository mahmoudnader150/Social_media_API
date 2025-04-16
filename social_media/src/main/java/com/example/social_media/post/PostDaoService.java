package com.example.social_media.post;

import com.example.social_media.user.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class PostDaoService {
    private static List<Post> posts = new ArrayList<>();
    private static long postsCount = 0;

    public List<Post> findAll() {
        return posts;
    }

    public List<Post> findByUser(User user) {
        Predicate<? super Post> predicate = post -> post.getUser().getId().equals(user.getId());
        return posts.stream().filter(predicate).toList();
    }
} 
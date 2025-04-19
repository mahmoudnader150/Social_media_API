package com.example.social_media.post;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PostAccessDeniedException extends RuntimeException {
    public PostAccessDeniedException(String message) {
        super(message);
    }
} 
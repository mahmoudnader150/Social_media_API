package com.example.social_media.user;

import com.example.social_media.security.CustomUserDetails;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class UserStatusHandler {
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public UserStatusHandler(UserService userService, SimpMessagingTemplate messagingTemplate) {
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/user.status")
    public void handleUserStatus(
            @Payload boolean isOnline,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            SimpMessageHeaderAccessor headerAccessor) {
        
        User user = userDetails.getUser();
        userService.updateUserStatus(user.getId(), isOnline);
        
        // Broadcast the status update to all users
        messagingTemplate.convertAndSend("/topic/user.status", new UserStatusUpdate(
            user.getId(),
            user.getName(),
            isOnline,
            isOnline ? null : LocalDateTime.now()
        ));
    }
} 
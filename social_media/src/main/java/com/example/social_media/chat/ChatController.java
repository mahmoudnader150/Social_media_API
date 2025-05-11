package com.example.social_media.chat;

import com.example.social_media.notification.NotificationService;
import com.example.social_media.user.User;
import com.example.social_media.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public ChatController(
            ChatService chatService, 
            UserService userService,
            NotificationService notificationService) {
        this.chatService = chatService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @MessageMapping("/send")
    public void sendMessage(@Payload Map<String, String> message, Authentication authentication) {
        // Validate the message payload
        if (!message.containsKey("receiverId") || !message.containsKey("content")) {
            throw new IllegalArgumentException("Missing required fields in message payload");
        }

        try {
            Long senderId = Long.parseLong(authentication.getName());
            Long receiverId = Long.parseLong(message.get("receiverId"));
            String content = message.get("content");
            
            // Get user objects for notification
            User sender = userService.findById(senderId);
            User receiver = userService.findById(receiverId);
            
            // Send the message
            Message chatMessage = chatService.sendMessage(senderId, receiverId, content);
            
            // Send notification for new message
            notificationService.notifyNewMessage(receiver, chatMessage.getId(), sender);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid user ID format", e);
        }
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Message>> getChatHistory(
            @PathVariable Long userId,
            Authentication authentication) {
        try {
            Long currentUserId = Long.parseLong(authentication.getName());
            List<Message> messages = chatService.getChatHistory(currentUserId, userId);
            return ResponseEntity.ok(messages);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid user ID format", e);
        }
    }

    @GetMapping("/unread/{senderId}")
    public ResponseEntity<List<Message>> getUnreadMessages(
            @PathVariable Long senderId,
            Authentication authentication) {
        try {
            Long currentUserId = Long.parseLong(authentication.getName());
            List<Message> messages = chatService.getUnreadMessages(currentUserId, senderId);
            return ResponseEntity.ok(messages);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid user ID format", e);
        }
    }
}
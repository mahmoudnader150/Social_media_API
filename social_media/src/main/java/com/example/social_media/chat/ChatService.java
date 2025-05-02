package com.example.social_media.chat;

import com.example.social_media.user.User;
import com.example.social_media.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatService(MessageRepository messageRepository, UserService userService, SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public Message sendMessage(Long senderId, Long receiverId, String content) {
        User sender = userService.getUserById(senderId);
        User receiver = userService.getUserById(receiverId);

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);

        Message savedMessage = messageRepository.save(message);

        // Send message to receiver's WebSocket topic
        messagingTemplate.convertAndSendToUser(
            receiver.getUsername(),
            "/queue/messages",
            savedMessage
        );

        return savedMessage;
    }

    @Transactional(readOnly = true)
    public List<Message> getChatHistory(Long userId1, Long userId2) {
        User user1 = userService.getUserById(userId1);
        User user2 = userService.getUserById(userId2);
        return messageRepository.findChatHistory(user1, user2);
    }

    @Transactional(readOnly = true)
    public List<Message> getUnreadMessages(Long userId, Long senderId) {
        User user = userService.getUserById(userId);
        User sender = userService.getUserById(senderId);
        return messageRepository.findUnreadMessages(user, sender);
    }
} 
package com.example.social_media.notification;

import com.example.social_media.user.User;
import com.example.social_media.user.UserFollowing;
import com.example.social_media.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserService userService,
            SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    public Notification createNotification(User user, String message, NotificationType type, Long referenceId, String referenceUrl) {
        Notification notification = new Notification(user, message, type, referenceId, referenceUrl);
        Notification savedNotification = notificationRepository.save(notification);
        
        // Send real-time notification through WebSocket
        NotificationDTO notificationDTO = new NotificationDTO(savedNotification);
        messagingTemplate.convertAndSendToUser(
                user.getUsername(),
                "/queue/notifications",
                notificationDTO
        );
        
        return savedNotification;
    }

    public void notifyNewPost(User postAuthor, Long postId) {
        // Get all followers of the post author
        List<UserFollowing> followers = postAuthor.getFollowers();
        
        for (UserFollowing follower : followers) {
            User user = follower.getFollower();
            String message = postAuthor.getName() + " posted something new";
            createNotification(
                    user,
                    message,
                    NotificationType.NEW_POST,
                    postId,
                    "/posts/" + postId
            );
        }
    }

    public void notifyNewLike(User postAuthor, Long postId, User likedBy) {
        if (!postAuthor.getId().equals(likedBy.getId())) {
            String message = likedBy.getName() + " liked your post";
            createNotification(
                    postAuthor,
                    message,
                    NotificationType.NEW_LIKE,
                    postId,
                    "/posts/" + postId
            );
        }
    }

    public void notifyNewMessage(User receiver, Long chatId, User sender) {
        String message = "New message from " + sender.getName();
        createNotification(
                receiver,
                message,
                NotificationType.NEW_MESSAGE,
                chatId,
                "/chat/" + chatId
        );
    }

    public Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(NotificationDTO::new);
    }

    public List<NotificationDTO> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc(userId, false)
                .stream()
                .map(NotificationDTO::new)
                .collect(Collectors.toList());
    }

    public long getUnreadNotificationCount(Long userId) {
        return notificationRepository.countByUserIdAndRead(userId, false);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc(userId, false);
        unreadNotifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }
} 
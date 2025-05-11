package com.example.social_media.notification;

import com.example.social_media.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<Page<NotificationDTO>> getUserNotifications(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(notificationService.getUserNotifications(currentUser.getUser().getId(), pageable));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(currentUser.getUser().getId()));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getUnreadNotificationCount(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        long count = notificationService.getUnreadNotificationCount(currentUser.getUser().getId());
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/read-all")
    public ResponseEntity<?> markAllAsRead(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        notificationService.markAllAsRead(currentUser.getUser().getId());
        return ResponseEntity.ok().build();
    }
} 
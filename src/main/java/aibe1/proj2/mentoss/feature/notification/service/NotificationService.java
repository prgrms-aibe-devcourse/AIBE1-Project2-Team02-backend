package aibe1.proj2.mentoss.feature.notification.service;

import aibe1.proj2.mentoss.feature.notification.model.dto.NotificationResponseDto;

import java.util.List;

public interface NotificationService {
    List<NotificationResponseDto> getRecentNotifications(Long receiverId);

    void createNotification(Long receiverId, Long senderId, String type, String content, Long referenceId, String targetUrl);

    void markAllAsRead(Long receiverId);
    int getUnreadCount(Long receiverId);
}
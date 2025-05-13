package aibe1.proj2.mentoss.feature.notification.model.dto;

import java.time.LocalDateTime;

public record NotificationResponseDto(
        Long notificationId,
        Long senderId,
        String nickname,
        String profileImage,
        String content,
        String type,
        String targetUrl,
        Boolean isRead,
        LocalDateTime createdAt
) {}
package aibe1.proj2.mentoss.feature.notification.model.dto;

import lombok.Builder;

@Builder
public record NotificationCreateRequestDto(
        Long receiverId,
        Long senderId,
        String type,
        String content,
        Long referenceId,
        String targetUrl
) {}
package aibe1.proj2.mentoss.feature.report.model.dto.response;

import java.time.LocalDateTime;

public record MessageDataResponseDto(
        Long messageId,
        Long senderId,
        Long receiverId,
        String content,
        boolean isRead,
        boolean isDeleted,
        LocalDateTime createdAt
) {
}

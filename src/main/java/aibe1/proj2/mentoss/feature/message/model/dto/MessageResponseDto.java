package aibe1.proj2.mentoss.feature.message.model.dto;

import aibe1.proj2.mentoss.global.entity.Message;

import java.time.LocalDateTime;

public record MessageResponseDto(
        Long messageId,
        Long senderId,
        Long receiverId,
        String nickname,
        String content,
        Boolean isRead,
        LocalDateTime createdAt,
        Boolean mine
) {
    public static MessageResponseDto fromEntity(Message e, Long userId) {
        return new MessageResponseDto(
                e.getMessageId(),
                e.getSenderId(),
                e.getReceiverId(),
                e.getNickname(),
                e.getContent(),
                e.getIsRead(),
                e.getCreatedAt(),
                userId.equals(e.getSenderId())
        );
    }
}
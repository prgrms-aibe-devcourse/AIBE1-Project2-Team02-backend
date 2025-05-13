package aibe1.proj2.mentoss.feature.message.model.dto;

import aibe1.proj2.mentoss.global.entity.Message;

import java.time.LocalDateTime;

public record MessageSendRequestDto(
        Long receiverId,
        String content,
        Boolean skipNotification
) {
    public Message toEntity(Long senderId) {
        Message m = new Message();
        m.setSenderId(senderId);
        m.setReceiverId(receiverId);
        m.setContent(content);
        m.setCreatedAt(LocalDateTime.now());
        return m;
    }
}


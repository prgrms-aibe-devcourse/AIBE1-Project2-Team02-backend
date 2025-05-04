package aibe1.proj2.mentoss.feature.message.model.dto;

import aibe1.proj2.mentoss.global.entity.Message;

public record MessageSendRequestDto(
        Long receiverId,
        String content
) {
    public Message toEntity(Long senderId) {
        Message m = new Message();
        m.setSenderId(senderId);
        m.setReceiverId(receiverId);
        m.setContent(content);
        return m;
    }
}


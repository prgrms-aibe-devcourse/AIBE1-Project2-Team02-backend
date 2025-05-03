package aibe1.proj2.mentoss.feature.message.service;

import aibe1.proj2.mentoss.feature.message.model.dto.MessageResponseDto;
import aibe1.proj2.mentoss.feature.message.model.dto.MessageSendRequestDto;
import aibe1.proj2.mentoss.feature.message.model.mapper.MessageMapper;
import aibe1.proj2.mentoss.global.entity.Message;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final MessageMapper messageMapper;

    @Override
    public List<MessageResponseDto> getSentMessages(Long senderId) {
        List<Message> messages = messageMapper.findSentMessages(senderId);
        return messages.stream()
                .map(message -> MessageResponseDto.fromEntity(message, senderId))
                .toList();
    }

    @Override
    public List<MessageResponseDto> getReceivedMessages(Long receiverId) {
        List<Message> messages = messageMapper.findReceivedMessages(receiverId);
        return messages.stream()
                .map(message -> MessageResponseDto.fromEntity(message, receiverId))
                .toList();
    }

    @Override
    public MessageResponseDto getMessage(Long messageId, Long userId) {
        Message message = messageMapper.findById(messageId);

        if (message.getReceiverId().equals(userId) && !message.getIsRead()) {
            messageMapper.markAsRead(messageId);
            message.setIsRead(true);
        }

        return MessageResponseDto.fromEntity(message, userId);
    }

    @Override
    public Long sendMessage(MessageSendRequestDto dto, Long senderId) {

        if (dto.receiverId() == null) {
            throw new IllegalArgumentException("받는 사용자 ID는 필수입니다.");
        }

        if (dto.content() == null || dto.content().trim().isEmpty()) {
            throw new IllegalArgumentException("쪽지 내용을 입력해주세요.");
        }

        Message message = dto.toEntity(senderId);
        messageMapper.insert(message);
        return message.getMessageId();
    }
}
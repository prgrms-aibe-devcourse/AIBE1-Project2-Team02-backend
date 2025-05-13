package aibe1.proj2.mentoss.feature.message.service;

import aibe1.proj2.mentoss.feature.message.model.dto.MessageResponseDto;
import aibe1.proj2.mentoss.feature.message.model.dto.MessageSendRequestDto;
import aibe1.proj2.mentoss.feature.message.model.dto.PageResponse;
import aibe1.proj2.mentoss.feature.message.model.mapper.MessageMapper;
import aibe1.proj2.mentoss.feature.notification.service.NotificationService;
import aibe1.proj2.mentoss.global.entity.Message;
import aibe1.proj2.mentoss.global.entity.enums.NotificationType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final MessageMapper messageMapper;
    private final NotificationService notificationService;
    @Override
    public PageResponse<MessageResponseDto> getSentMessages(Long senderId, int page, int size, String filterBy , String keyword) {
        int offset = page * size;
        List<Message> messages = messageMapper.findSentMessages(senderId, size, offset, filterBy, keyword);
        int totalCount = messageMapper.countSentMessages(senderId, filterBy, keyword);
        List<MessageResponseDto> content = messages.stream()
                .map(m -> MessageResponseDto.fromEntity(m, senderId))
                .toList();

        return PageResponse.of(content, page, size, totalCount);
    }

    @Override
    public PageResponse<MessageResponseDto> getReceivedMessages(Long receiverId, int page, int size, String filterBy , String keyword) {
        int offset = page * size;
        List<Message> messages = messageMapper.findReceivedMessages(receiverId, size, offset, filterBy, keyword);
        int totalCount = messageMapper.countReceivedMessages(receiverId, filterBy, keyword);
        List<MessageResponseDto> content = messages.stream()
                .map(message -> MessageResponseDto.fromEntity(message, receiverId))
                .toList();
        return PageResponse.of(content, page, size, totalCount);
    }

    @Override
    public MessageResponseDto getMessage(Long messageId, Long userId) {
        Message message = messageMapper.findById(messageId, userId);

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

        if (dto.skipNotification() == null || !dto.skipNotification()) {
            String content = "새로운 쪽지가 도착했어요.";
            notificationService.createNotification(
                    dto.receiverId(),
                    senderId,
                    NotificationType.MESSAGE.name(),
                    content,
                    message.getMessageId(),
                    "/messages"
            );
        }

        return message.getMessageId();
    }
}